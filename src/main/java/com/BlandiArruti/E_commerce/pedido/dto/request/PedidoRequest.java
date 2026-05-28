package com.BlandiArruti.E_commerce.pedido.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(
        @NotNull(message = "El id de cliente es obligatorio.")
        Long idCliente,

        @NotEmpty(message = "El pedido debe tener al menos un ítem.")
        @Valid
        List<ItemPedidoRequest> items
) {}
