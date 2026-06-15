package com.BlandiArruti.E_commerce.factura.controller;

import com.BlandiArruti.E_commerce.enums.TipoFactura;
import com.BlandiArruti.E_commerce.factura.dto.response.FacturaResponse;
import com.BlandiArruti.E_commerce.factura.service.IFacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factura")
@RequiredArgsConstructor
public class FacturaController {

    private final IFacturaService facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaResponse>> listarTodas(
            @RequestParam(required = false) TipoFactura tipo,
            @RequestParam(required = false) Long idPedido,
            @RequestParam(required = false) Long idCliente) {
        return ResponseEntity.ok(facturaService.listarTodas(tipo, idPedido, idCliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.buscarPorId(id));
    }
}
