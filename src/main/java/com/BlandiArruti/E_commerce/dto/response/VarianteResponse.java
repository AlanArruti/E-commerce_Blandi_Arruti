package com.BlandiArruti.E_commerce.dto.response;

import java.util.Map;

public record VarianteResponse(
        Long idVariante,
        Map<String, String> atributos,
        Integer stock,
        Long idProducto
) {}
