package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.response.PedidoResponse;
import com.BlandiArruti.E_commerce.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ItemPedidoMapper.class, EnvioMapper.class, FacturaMapper.class})
public interface PedidoMapper {

    @Mapping(source = "id", target = "idPedido")
    @Mapping(source = "estadoPedido", target = "estado")
    @Mapping(source = "items", target = "items")
    PedidoResponse toResponse(Pedido pedido);
}
