package com.BlandiArruti.E_commerce.carrito.controller;

import com.BlandiArruti.E_commerce.carrito.dto.request.CantidadCarritoRequest;
import com.BlandiArruti.E_commerce.carrito.dto.request.ItemCarritoRequest;
import com.BlandiArruti.E_commerce.carrito.dto.response.CarritoResponse;
import com.BlandiArruti.E_commerce.carrito.service.CarritoService;
import com.BlandiArruti.E_commerce.pedido.dto.response.PedidoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cliente/{idCliente}/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    public ResponseEntity<CarritoResponse> obtenerCarrito(@PathVariable Long idCliente) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(idCliente));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> agregarItem(@PathVariable Long idCliente,
                                                        @Valid @RequestBody ItemCarritoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarItem(idCliente, request));
    }

    @PutMapping("/items/{idItem}")
    public ResponseEntity<CarritoResponse> actualizarCantidad(@PathVariable Long idCliente,
                                                               @PathVariable Long idItem,
                                                               @Valid @RequestBody CantidadCarritoRequest request) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(idCliente, idItem, request));
    }

    @DeleteMapping("/items/{idItem}")
    public ResponseEntity<CarritoResponse> eliminarItem(@PathVariable Long idCliente,
                                                         @PathVariable Long idItem) {
        return ResponseEntity.ok(carritoService.eliminarItem(idCliente, idItem));
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long idCliente) {
        carritoService.vaciarCarrito(idCliente);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponse> checkout(@PathVariable Long idCliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.checkout(idCliente));
    }
}
