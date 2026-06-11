package com.BlandiArruti.E_commerce.cliente.service;

import com.BlandiArruti.E_commerce.cliente.dto.request.ClienteRequest;
import com.BlandiArruti.E_commerce.cliente.dto.request.DireccionRequest;
import com.BlandiArruti.E_commerce.cliente.dto.response.ClienteResponse;
import com.BlandiArruti.E_commerce.cliente.dto.response.DireccionResponse;
import com.BlandiArruti.E_commerce.cliente.entity.Cliente;
import com.BlandiArruti.E_commerce.cliente.entity.Direccion;
import com.BlandiArruti.E_commerce.cliente.mapper.ClienteMapper;
import com.BlandiArruti.E_commerce.cliente.mapper.DireccionMapper;
import com.BlandiArruti.E_commerce.cliente.repository.ClienteRepository;
import com.BlandiArruti.E_commerce.cliente.repository.DireccionRepository;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import com.BlandiArruti.E_commerce.exception.DuplicadoException;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.geo.entity.Ciudad;
import com.BlandiArruti.E_commerce.geo.repository.CiudadRepository;
import com.BlandiArruti.E_commerce.pedido.dto.response.PedidoResponse;
import com.BlandiArruti.E_commerce.pedido.mapper.PedidoMapper;
import com.BlandiArruti.E_commerce.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;
    private final CiudadRepository ciudadRepository;
    private final PedidoRepository pedidoRepository;
    private final ClienteMapper clienteMapper;
    private final DireccionMapper direccionMapper;
    private final PedidoMapper pedidoMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAllByActivoTrue().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return clienteMapper.toResponse(
                clienteRepository.findByIdAndActivoTrue(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.cliente(id))
        );
    }

    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.email())) {
            throw DuplicadoException.email(request.email());
        }
        if (clienteRepository.existsByDni(request.dni())) {
            throw DuplicadoException.dni(request.dni());
        }
        Cliente cliente = clienteMapper.toEntity(request);
        cliente.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(id));

        if (!cliente.getEmail().equals(request.email())
                && clienteRepository.existsByEmail(request.email())) {
            throw DuplicadoException.email(request.email());
        }

        cliente.setNombre(request.nombre());
        cliente.setApellido(request.apellido());
        cliente.setEmail(request.email());
        cliente.setContrasenia(passwordEncoder.encode(request.contrasenia()));
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<DireccionResponse> listarDirecciones(Long idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(idCliente));
        return cliente.getDirecciones().stream()
                .map(direccionMapper::toResponse)
                .toList();
    }

    public DireccionResponse agregarDireccion(Long idCliente, DireccionRequest request) {
        Cliente cliente = clienteRepository.findByIdAndActivoTrue(idCliente)
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(idCliente));
        Ciudad ciudad = ciudadRepository.findById(request.idCiudad())
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Ciudad con id " + request.idCiudad() + " no encontrada."));

        Direccion direccion = Direccion.builder()
                .nombreCalle(request.nombreCalle())
                .numeroCalle(request.numeroCalle())
                .ciudad(ciudad)
                .cliente(cliente)
                .build();

        return direccionMapper.toResponse(direccionRepository.save(direccion));
    }

    public void eliminarDireccion(Long idCliente, Long idDireccion) {
        if (clienteRepository.findByIdAndActivoTrue(idCliente).isEmpty()) {
            throw EntidadNoEncontradaException.cliente(idCliente);
        }
        Direccion direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> EntidadNoEncontradaException.direccion(idDireccion));
        direccionRepository.delete(direccion);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> historialPedidos(Long idCliente, EstadoPedido estado) {
        if (clienteRepository.findByIdAndActivoTrue(idCliente).isEmpty()) {
            throw EntidadNoEncontradaException.cliente(idCliente);
        }
        var pedidos = estado != null
                ? pedidoRepository.findByClienteIdAndEstadoPedido(idCliente, estado)
                : pedidoRepository.findByClienteId(idCliente);

        return pedidos.stream()
                .map(pedidoMapper::toResponse)
                .toList();
    }
}
