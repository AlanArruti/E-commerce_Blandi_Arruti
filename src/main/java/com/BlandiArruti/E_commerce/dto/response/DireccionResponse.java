package com.BlandiArruti.E_commerce.dto.response;

public record DireccionResponse(
        Long idDireccion,
        String nombreCalle,
        Integer numeroCalle,
        CiudadResponse ciudad
) {}
