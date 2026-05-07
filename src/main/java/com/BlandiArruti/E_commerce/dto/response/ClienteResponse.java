package com.BlandiArruti.E_commerce.dto.response;

import java.util.List;

public record ClienteResponse(
        Long idCliente,
        String nombre,
        String apellido,
        String dni,
        String email,
        List<DireccionResponse> direcciones
) {}
