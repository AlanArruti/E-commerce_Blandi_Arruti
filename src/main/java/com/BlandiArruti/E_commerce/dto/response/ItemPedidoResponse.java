package com.BlandiArruti.E_commerce.dto.response;

public record ItemPedidoResponse(
        Long idItemPedido,
        ProductoResponse producto,
        VarianteResponse variante,
        Integer cantidad,
        Double precioTotalProducto
) {}
