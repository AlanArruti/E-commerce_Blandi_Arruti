package com.BlandiArruti.E_commerce.service;

import com.BlandiArruti.E_commerce.dto.request.*;
import com.BlandiArruti.E_commerce.dto.response.EnvioResponse;
import com.BlandiArruti.E_commerce.dto.response.FacturaResponse;
import com.BlandiArruti.E_commerce.dto.response.PedidoResponse;
import com.BlandiArruti.E_commerce.entity.*;
import com.BlandiArruti.E_commerce.enums.EstadoEnvio;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import com.BlandiArruti.E_commerce.exception.*;
import com.BlandiArruti.E_commerce.mapper.EnvioMapper;
import com.BlandiArruti.E_commerce.mapper.FacturaMapper;
import com.BlandiArruti.E_commerce.mapper.PedidoMapper;
import com.BlandiArruti.E_commerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VarianteRepository varianteRepository;
    private final FacturaRepository facturaRepository;
    private final EnvioRepository envioRepository;
    private final DireccionRepository direccionRepository;
    private final PedidoMapper pedidoMapper;
    private final FacturaMapper facturaMapper;
    private final EnvioMapper envioMapper;

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos(EstadoPedido estado, Long clienteId) {
        if (estado != null && clienteId != null) {
            return pedidoRepository.findByClienteIdAndEstadoPedido(clienteId, estado)
                    .stream().map(pedidoMapper::toResponse).toList();
        } else if (estado != null) {
            return pedidoRepository.findByEstadoPedido(estado)
                    .stream().map(pedidoMapper::toResponse).toList();
        } else if (clienteId != null) {
            return pedidoRepository.findByClienteId(clienteId)
                    .stream().map(pedidoMapper::toResponse).toList();
        }
        return pedidoRepository.findAll().stream().map(pedidoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return pedidoMapper.toResponse(
                pedidoRepository.findById(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.pedido(id))
        );
    }

    public PedidoResponse crear(PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.idCliente())
                .orElseThrow(() -> EntidadNoEncontradaException.cliente(request.idCliente()));

        // 1. Validar stock y reunir entidades en un solo recorrido
        List<Producto> productos = new ArrayList<>();
        List<Variante> variantes = new ArrayList<>();

        for (ItemPedidoRequest itemReq : request.items()) {
            Producto producto = productoRepository.findById(itemReq.idProducto())
                    .orElseThrow(() -> EntidadNoEncontradaException.producto(itemReq.idProducto()));
            Variante variante = varianteRepository.findById(itemReq.idVariante())
                    .orElseThrow(() -> EntidadNoEncontradaException.variante(itemReq.idVariante()));

            if (variante.getStock() < itemReq.cantidad()) {
                throw StockInsuficienteException.stockInsuficiente(
                        itemReq.idVariante(), variante.getStock(), itemReq.cantidad());
            }
            productos.add(producto);
            variantes.add(variante);
        }

        // 2. Construir pedido con sus items
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estadoPedido(EstadoPedido.PENDIENTE_PAGO)
                .build();

        for (int i = 0; i < request.items().size(); i++) {
            ItemPedidoRequest itemReq = request.items().get(i);
            ItemPedido item = ItemPedido.builder()
                    .pedido(pedido)
                    .producto(productos.get(i))
                    .variante(variantes.get(i))
                    .cantidad(itemReq.cantidad())
                    .precioProducto(productos.get(i).getPrecio() * itemReq.cantidad())
                    .build();
            pedido.getItems().add(item);
        }

        return pedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    public void cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(id));

        EstadoPedido estado = pedido.getEstadoPedido();

        if (estado == EstadoPedido.ENTREGADO || estado == EstadoPedido.CANCELADO) {
            throw PedidoNoModificableException.noCancelable(id, estado);
        }

        // Si ya se descontó stock (en preparacion, pagado o despachado), devolverlo
        if (estado == EstadoPedido.EN_PREPARACION || estado == EstadoPedido.PAGADO || estado == EstadoPedido.DESPACHADO) {
            for (ItemPedido item : pedido.getItems()) {
                Variante variante = item.getVariante();
                variante.setStock(variante.getStock() + item.getCantidad());
                varianteRepository.save(variante);
            }
        }

        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    public PedidoResponse pagar(Long id, PagoRequest request) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(id));

        if (pedido.getEstadoPedido() != EstadoPedido.PENDIENTE_PAGO) {
            throw PedidoNoModificableException.porEstado(id, pedido.getEstadoPedido());
        }

        // Descontar stock
        for (ItemPedido item : pedido.getItems()) {
            Variante variante = item.getVariante();
            if (variante.getStock() < item.getCantidad()) {
                throw StockInsuficienteException.stockInsuficiente(
                        variante.getId(), variante.getStock(), item.getCantidad());
            }
            variante.setStock(variante.getStock() - item.getCantidad());
            varianteRepository.save(variante);
        }

        // Generar factura
        double total = pedido.getItems().stream()
                .mapToDouble(ItemPedido::getPrecioProducto)
                .sum();

        Factura factura = Factura.builder()
                .pedido(pedido)
                .tipoFactura(request.tipoFactura())
                .precioTotal(total)
                .build();
        facturaRepository.save(factura);

        pedido.setEstadoPedido(EstadoPedido.PAGADO);
        pedido.setFactura(factura);
        return pedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    public PedidoResponse cambiarEstado(Long id, EstadoPedidoRequest request) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(id));

        EstadoPedido actual = pedido.getEstadoPedido();
        EstadoPedido nuevo = request.estado();

        boolean valida = switch (actual) {
            case PENDIENTE_PAGO -> nuevo == EstadoPedido.EN_PREPARACION || nuevo == EstadoPedido.CANCELADO;
            case EN_PREPARACION -> nuevo == EstadoPedido.PAGADO || nuevo == EstadoPedido.CANCELADO;
            case PAGADO         -> nuevo == EstadoPedido.DESPACHADO;
            case DESPACHADO     -> nuevo == EstadoPedido.ENTREGADO || nuevo == EstadoPedido.CANCELADO;
            default             -> false;
        };

        if (!valida) {
            throw new EcommerceException(
                    "Transición de estado inválida: " + actual + " → " + nuevo + ".");
        }

        pedido.setEstadoPedido(nuevo);
        return pedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public FacturaResponse obtenerFactura(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(idPedido));
        if (pedido.getFactura() == null) {
            throw new ConflictoException("El pedido con id " + idPedido + " aún no tiene factura.");
        }
        return facturaMapper.toResponse(pedido.getFactura());
    }

    // Envio

    @Transactional(readOnly = true)
    public EnvioResponse obtenerEnvio(Long idPedido) {
        Envio envio = envioRepository.findByPedidoId(idPedido)
                .orElseThrow(() -> EntidadNoEncontradaException.envio(idPedido));
        return envioMapper.toResponse(envio);
    }

    public EnvioResponse crearEnvio(Long idPedido, EnvioRequest request) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(idPedido));

        if (pedido.getEstadoPedido() != EstadoPedido.PAGADO) {
            throw new EcommerceException(
                    "Solo se puede crear un envío para pedidos en estado PAGADO.");
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
        envioRepository.save(envio);

        pedido.setEstadoPedido(EstadoPedido.DESPACHADO);
        pedido.setEnvio(envio);
        pedidoRepository.save(pedido);

        return envioMapper.toResponse(envio);
    }

    public EnvioResponse actualizarEstadoEnvio(Long idPedido, EstadoEnvioRequest request) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> EntidadNoEncontradaException.pedido(idPedido));
        Envio envio = envioRepository.findByPedidoId(idPedido)
                .orElseThrow(() -> EntidadNoEncontradaException.envio(idPedido));

        EstadoEnvio actual = envio.getEstado();
        EstadoEnvio nuevo = request.estado();

        boolean valida = switch (actual) {
            case DESPACHADO -> nuevo == EstadoEnvio.EN_CAMINO;
            case EN_CAMINO  -> nuevo == EstadoEnvio.ENTREGADO;
            default         -> false;
        };

        if (!valida) {
            throw new EcommerceException(
                    "Transición de estado de envío inválida: " + actual + " → " + nuevo + ".");
        }

        envio.setEstado(nuevo);

        if (nuevo == EstadoEnvio.ENTREGADO) {
            pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
            pedidoRepository.save(pedido);
        }

        return envioMapper.toResponse(envioRepository.save(envio));
    }
}
