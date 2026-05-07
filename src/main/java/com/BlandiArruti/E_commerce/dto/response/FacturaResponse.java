package com.BlandiArruti.E_commerce.dto.response;

import com.BlandiArruti.E_commerce.enums.TipoFactura;

public record FacturaResponse(
        String uuidFactura,
        TipoFactura tipo,
        Double precioTotal,
        Long idPedido
) {}
