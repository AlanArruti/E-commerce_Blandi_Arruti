package com.BlandiArruti.E_commerce.producto.controller;

import com.BlandiArruti.E_commerce.producto.dto.request.ProductoRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.StockRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.VarianteRequest;
import com.BlandiArruti.E_commerce.producto.dto.response.ProductoResponse;
import com.BlandiArruti.E_commerce.producto.dto.response.VarianteResponse;
import com.BlandiArruti.E_commerce.producto.service.IProductoService;
import com.BlandiArruti.E_commerce.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Gestión de productos y sus variantes")
@RestController
@RequestMapping("/api/v1/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoService;

    @Operation(summary = "Listar productos", description = "Filtrables por categoría, precio y nombre.")
    @GetMapping
    public ResponseEntity<PageResponse<ProductoResponse>> listarTodos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productoService.listarTodos(categoriaId, precioMin, precioMax, search, PageRequest.of(page, size)));
    }

    @Operation(summary = "Buscar producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(summary = "Crear producto (solo ADMIN)")
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @Operation(summary = "Actualizar producto (solo ADMIN)")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @Operation(summary = "Dar de baja producto (soft delete, solo ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar variantes del producto")
    @GetMapping("/{id}/variante")
    public ResponseEntity<List<VarianteResponse>> listarVariantes(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.listarVariantes(id));
    }

    @Operation(summary = "Agregar variante al producto (solo ADMIN)")
    @PostMapping("/{id}/variante")
    public ResponseEntity<VarianteResponse> agregarVariante(@PathVariable Long id,
                                                            @Valid @RequestBody VarianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.agregarVariante(id, request));
    }

    @Operation(summary = "Actualizar variante (solo ADMIN)")
    @PutMapping("/{idProducto}/variante/{idVariante}")
    public ResponseEntity<VarianteResponse> actualizarVariante(@PathVariable Long idProducto,
                                                               @PathVariable Long idVariante,
                                                               @Valid @RequestBody VarianteRequest request) {
        return ResponseEntity.ok(productoService.actualizarVariante(idProducto, idVariante, request));
    }

    @Operation(summary = "Eliminar variante (solo ADMIN)")
    @DeleteMapping("/{idProducto}/variante/{idVariante}")
    public ResponseEntity<Void> eliminarVariante(@PathVariable Long idProducto,
                                                 @PathVariable Long idVariante) {
        productoService.eliminarVariante(idProducto, idVariante);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ajustar stock de variante (solo ADMIN)", description = "Operaciones: AGREGAR o REDUCIR.")
    @PatchMapping("/{idProducto}/variante/{idVariante}/stock")
    public ResponseEntity<VarianteResponse> ajustarStock(@PathVariable Long idProducto,
                                                         @PathVariable Long idVariante,
                                                         @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(productoService.ajustarStock(idProducto, idVariante, request));
    }
}
