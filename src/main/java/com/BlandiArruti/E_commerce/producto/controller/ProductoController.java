package com.BlandiArruti.E_commerce.producto.controller;

import com.BlandiArruti.E_commerce.producto.dto.request.ProductoRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.StockRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.VarianteRequest;
import com.BlandiArruti.E_commerce.producto.dto.response.ProductoResponse;
import com.BlandiArruti.E_commerce.producto.dto.response.VarianteResponse;
import com.BlandiArruti.E_commerce.producto.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(productoService.listarTodos(categoriaId, precioMin, precioMax, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/variante")
    public ResponseEntity<List<VarianteResponse>> listarVariantes(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.listarVariantes(id));
    }

    @PostMapping("/{id}/variante")
    public ResponseEntity<VarianteResponse> agregarVariante(@PathVariable Long id,
                                                            @Valid @RequestBody VarianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.agregarVariante(id, request));
    }

    @PutMapping("/{idProducto}/variante/{idVariante}")
    public ResponseEntity<VarianteResponse> actualizarVariante(@PathVariable Long idProducto,
                                                               @PathVariable Long idVariante,
                                                               @Valid @RequestBody VarianteRequest request) {
        return ResponseEntity.ok(productoService.actualizarVariante(idProducto, idVariante, request));
    }

    @DeleteMapping("/{idProducto}/variante/{idVariante}")
    public ResponseEntity<Void> eliminarVariante(@PathVariable Long idProducto,
                                                 @PathVariable Long idVariante) {
        productoService.eliminarVariante(idProducto, idVariante);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idProducto}/variante/{idVariante}/stock")
    public ResponseEntity<VarianteResponse> ajustarStock(@PathVariable Long idProducto,
                                                         @PathVariable Long idVariante,
                                                         @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(productoService.ajustarStock(idProducto, idVariante, request));
    }
}
