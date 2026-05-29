package com.BlandiArruti.E_commerce.envio.service;

import com.BlandiArruti.E_commerce.cliente.entity.Direccion;
import com.BlandiArruti.E_commerce.cliente.repository.DireccionRepository;
import com.BlandiArruti.E_commerce.envio.dto.request.EnvioRequest;
import com.BlandiArruti.E_commerce.envio.dto.request.EstadoEnvioRequest;
import com.BlandiArruti.E_commerce.envio.dto.response.EnvioResponse;
import com.BlandiArruti.E_commerce.envio.entity.Envio;
import com.BlandiArruti.E_commerce.envio.mapper.EnvioMapper;
import com.BlandiArruti.E_commerce.envio.repository.EnvioRepository;
import com.BlandiArruti.E_commerce.enums.EstadoEnvio;
import com.BlandiArruti.E_commerce.exception.ConflictoException;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.pedido.entity.Pedido;
import com.BlandiArruti.E_commerce.pedido.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final EnvioMapper envioMapper;
    private final PedidoRepository pedidoRepository;
    private final DireccionRepository direccionRepository;

    @Transactional(readOnly = true)
    public List<EnvioResponse> listarTodos() {
        return envioRepository.findAll().stream()
                .map(envioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnvioResponse buscarPorId(Long id) {
        return envioMapper.toResponse(
                envioRepository.findById(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.envio(id))
        );
    }

    public EnvioResponse crear(Long idPedido, EnvioRequest request) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(idPedido));

        if (envioRepository.findByPedidoId(idPedido).isPresent()) {
            throw new ConflictoException("El pedido con id " + idPedido + " ya tiene un envío asignado.");
        }

        Direccion direccion = direccionRepository.findById(request.idDireccion())
                .orElseThrow(() -> EntidadNoEncontradaException.direccion(request.idDireccion()));

        Envio envio = Envio.builder()
                .pedido(pedido)
                .direccion(direccion)
                .estado(EstadoEnvio.DESPACHADO)
                .fechaSalida(request.fechaSalida())
                .fechaLlegada(request.fechaLlegada())
                .build();

        return envioMapper.toResponse(envioRepository.save(envio));
    }

    public EnvioResponse actualizarEstado(Long id, EstadoEnvioRequest request) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.envio(id));
        envio.setEstado(request.estado());
        return envioMapper.toResponse(envioRepository.save(envio));
    }

    public void eliminar(Long id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.envio(id));
        envioRepository.delete(envio);
    }
}
