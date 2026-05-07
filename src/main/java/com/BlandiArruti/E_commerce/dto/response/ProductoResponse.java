package com.BlandiArruti.E_commerce.dto.response;

import java.util.List;

public record ProductoResponse(
        Long idProducto,
        String nombre,
        String descripcion,
        Double precioBase,
        CategoriaResponse categoria,
        List<VarianteResponse> variantes
) {}
