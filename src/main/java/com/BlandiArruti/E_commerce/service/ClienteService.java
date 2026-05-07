package com.BlandiArruti.E_commerce.service;

import com.BlandiArruti.E_commerce.dto.request.ClienteRequest;
import com.BlandiArruti.E_commerce.dto.request.DireccionRequest;
import com.BlandiArruti.E_commerce.dto.response.ClienteResponse;
import com.BlandiArruti.E_commerce.dto.response.DireccionResponse;
import com.BlandiArruti.E_commerce.dto.response.PedidoResponse;
import com.BlandiArruti.E_commerce.entity.Ciudad;
import com.BlandiArruti.E_commerce.entity.Cliente;
import com.BlandiArruti.E_commerce.entity.Direccion;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import com.BlandiArruti.E_commerce.exception.DuplicadoException;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.mapper.ClienteMapper;
import com.BlandiArruti.E_commerce.mapper.DireccionMapper;
import com.BlandiArruti.E_commerce.mapper.PedidoMapper;
import com.BlandiArruti.E_commerce.repository.CiudadRepository;
import com.BlandiArruti.E_commerce.repository.ClienteRepository;
import com.BlandiArruti.E_commerce.repository.DireccionRepository;
import com.BlandiArruti.E_commerce.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
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


    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return clienteMapper.toResponse(
                clienteRepository.findById(id)
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
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(id));

        // Validar unicidad de email solo si cambió
        if (!cliente.getEmail().equals(request.email())
                && clienteRepository.existsByEmail(request.email())) {
            throw DuplicadoException.email(request.email());
        }

        cliente.setNombre(request.nombre());
        cliente.setApellido(request.apellido());
        cliente.setEmail(request.email());
        cliente.setContrasenia(request.contrasenia());
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(id));
        clienteRepository.delete(cliente);
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
        Cliente cliente = clienteRepository.findById(idCliente)
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
        if (!clienteRepository.existsById(idCliente)) {
            throw EntidadNoEncontradaException.cliente(idCliente);
        }
        Direccion direccion = direccionRepository.findById(idDireccion)
                .orElseThrow(() -> EntidadNoEncontradaException.direccion(idDireccion));
        direccionRepository.delete(direccion);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> historialPedidos(Long idCliente, EstadoPedido estado) {
        if (!clienteRepository.existsById(idCliente)) {
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
