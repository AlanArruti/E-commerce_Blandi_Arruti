package com.BlandiArruti.E_commerce.geo.controller;

import com.BlandiArruti.E_commerce.geo.dto.response.PaisResponse;
import com.BlandiArruti.E_commerce.geo.dto.response.ProvinciaResponse;
import com.BlandiArruti.E_commerce.geo.service.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paises")
@RequiredArgsConstructor
public class PaisController {

    private final GeoService geoService;

    @GetMapping
    public ResponseEntity<List<PaisResponse>> listarTodos() {
        return ResponseEntity.ok(geoService.listarPaises());
    }

    @GetMapping("/{id}/provincias")
    public ResponseEntity<List<ProvinciaResponse>> listarProvincias(@PathVariable Long id) {
        return ResponseEntity.ok(geoService.listarProvinciasDePais(id));
    }
}
