package com.BlandiArruti.E_commerce.dto.response;

public record CiudadResponse(
        Long idCiudad,
        String nombreCiudad,
        ProvinciaResponse provincia
) {}
