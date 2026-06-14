package com.BlandiArruti.E_commerce.pedido.controller;

import com.BlandiArruti.E_commerce.auth.service.UsuarioDetails;
import com.BlandiArruti.E_commerce.envio.dto.request.EnvioRequest;
import com.BlandiArruti.E_commerce.envio.dto.request.EstadoEnvioRequest;
import com.BlandiArruti.E_commerce.envio.dto.response.EnvioResponse;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import com.BlandiArruti.E_commerce.factura.dto.response.FacturaResponse;
import com.BlandiArruti.E_commerce.mercadopago.dto.response.PreferenciaResponse;
import com.BlandiArruti.E_commerce.pedido.dto.request.EstadoPedidoRequest;
import com.BlandiArruti.E_commerce.pedido.dto.request.PagoRequest;
import com.BlandiArruti.E_commerce.pedido.dto.request.PedidoRequest;
import com.BlandiArruti.E_commerce.pedido.dto.response.PedidoResponse;
import com.BlandiArruti.E_commerce.pedido.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos(
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(required = false) Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarTodos(estado, clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(
            @Valid @RequestBody PedidoRequest request,
            @AuthenticationPrincipal UsuarioDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(request, principal));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/iniciar-pago")
    public ResponseEntity<PreferenciaResponse> iniciarPago(@PathVariable Long id,
                                                           @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pedidoService.iniciarPagoMercadoPago(id, request));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<PedidoResponse> pagar(@PathVariable Long id,
                                                @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pedidoService.pagar(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(@PathVariable Long id,
                                                        @Valid @RequestBody EstadoPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, request));
    }

    @GetMapping("/{id}/factura")
    public ResponseEntity<FacturaResponse> obtenerFactura(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerFactura(id));
    }

    @GetMapping("/{id}/envio")
    public ResponseEntity<EnvioResponse> obtenerEnvio(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerEnvio(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/envio")
    public ResponseEntity<EnvioResponse> crearEnvio(@PathVariable Long id,
                                                    @Valid @RequestBody EnvioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearEnvio(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/envio/estado")
    public ResponseEntity<EnvioResponse> actualizarEstadoEnvio(@PathVariable Long id,
                                                               @Valid @RequestBody EstadoEnvioRequest request) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoEnvio(id, request));
    }
}
