package com.BlandiArruti.E_commerce.dto.response;

import com.BlandiArruti.E_commerce.enums.EstadoPedido;

import java.util.List;

public record PedidoResponse(
        String uuidPedido,
        ClienteResponse cliente,
        List<ItemPedidoResponse> items,
        EstadoPedido estado,
        EnvioResponse envio,
        FacturaResponse factura
) {}
