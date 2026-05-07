package com.BlandiArruti.E_commerce.dto.response;

public record DireccionResponse(
        String uuidDireccion,
        String nombreCalle,
        Integer numeroCalle,
        CiudadResponse ciudad
) {}
