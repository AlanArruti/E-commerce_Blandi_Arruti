package com.BlandiArruti.E_commerce.geo.controller;

import com.BlandiArruti.E_commerce.geo.dto.response.CiudadResponse;
import com.BlandiArruti.E_commerce.geo.service.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provincias")
@RequiredArgsConstructor
public class ProvinciaController {

    private final GeoService geoService;

    @GetMapping("/{id}/ciudades")
    public ResponseEntity<List<CiudadResponse>> listarCiudades(@PathVariable Long id) {
        return ResponseEntity.ok(geoService.listarCiudadesDeProvincia(id));
    }
}
