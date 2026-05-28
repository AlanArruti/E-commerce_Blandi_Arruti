package com.BlandiArruti.E_commerce.producto.dto.response;

import com.BlandiArruti.E_commerce.categoria.dto.response.CategoriaResponse;

import java.util.List;

public record ProductoResponse(
        String uuidProducto,
        String nombre,
        String descripcion,
        Double precioBase,
        CategoriaResponse categoria,
        List<VarianteResponse> variantes
) {}
