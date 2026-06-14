package com.BlandiArruti.E_commerce.cotizacion.controller;

import com.BlandiArruti.E_commerce.cotizacion.dto.response.CotizacionEnvioResponse;
import com.BlandiArruti.E_commerce.cotizacion.service.CotizacionEnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cotizacion")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionEnvioService cotizacionEnvioService;

    @GetMapping("/envio/{idDireccion}")
    public ResponseEntity<List<CotizacionEnvioResponse>> cotizarEnvio(@PathVariable Long idDireccion) {
        return ResponseEntity.ok(cotizacionEnvioService.cotizarPorDireccion(idDireccion));
    }
}
