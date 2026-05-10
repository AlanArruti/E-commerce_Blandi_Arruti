package com.BlandiArruti.E_commerce.dto.response;

import java.util.List;

public record ClienteResponse(
        String uuidCliente,
        String nombre,
        String apellido,
        String dni,
        String email,
        List<DireccionResponse> direcciones
) {}
