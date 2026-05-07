package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.response.ItemPedidoResponse;
import com.BlandiArruti.E_commerce.entity.ItemPedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class, VarianteMapper.class})
public interface ItemPedidoMapper {

    @Mapping(source = "id", target = "idItemPedido")
    @Mapping(source = "precioProducto", target = "precioTotalProducto")
    ItemPedidoResponse toResponse(ItemPedido item);
}
