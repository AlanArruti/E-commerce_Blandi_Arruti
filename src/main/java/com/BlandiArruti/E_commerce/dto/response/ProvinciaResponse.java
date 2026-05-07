package com.BlandiArruti.E_commerce.dto.response;

public record ProvinciaResponse(
        Long idProvincia,
        String nombreProvincia,
        PaisResponse pais
) {}
