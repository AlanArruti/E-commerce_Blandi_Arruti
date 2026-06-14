package com.BlandiArruti.E_commerce.factura.controller;

import com.BlandiArruti.E_commerce.enums.TipoFactura;
import com.BlandiArruti.E_commerce.factura.dto.request.FacturaResponse;
import com.BlandiArruti.E_commerce.factura.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factura")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaResponse>> listarTodas(
            @RequestParam(required = false) TipoFactura tipo,
            @RequestParam(required = false) Long idPedido) {
        return ResponseEntity.ok(facturaService.listarTodas(tipo, idPedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }
}
