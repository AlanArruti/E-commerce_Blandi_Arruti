package com.BlandiArruti.E_commerce.cliente.dto.response;

import com.BlandiArruti.E_commerce.geo.dto.response.CiudadResponse;

public record DireccionResponse(
        String uuidDireccion,
        String nombreCalle,
        Integer numeroCalle,
        CiudadResponse ciudad
) {}
