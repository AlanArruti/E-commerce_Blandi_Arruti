package com.BlandiArruti.E_commerce.pedido.dto.response;

import com.BlandiArruti.E_commerce.cliente.dto.response.ClienteResponse;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import com.BlandiArruti.E_commerce.envio.dto.response.EnvioResponse;
import com.BlandiArruti.E_commerce.factura.dto.response.FacturaResponse;

import java.util.List;

public record PedidoResponse(
        String uuidPedido,
        ClienteResponse cliente,
        List<ItemPedidoResponse> items,
        EstadoPedido estado,
        EnvioResponse envio,
        FacturaResponse factura
) {}
