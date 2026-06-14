package com.BlandiArruti.E_commerce.pedido.mapper;

import com.BlandiArruti.E_commerce.cliente.mapper.ClienteMapper;
import com.BlandiArruti.E_commerce.envio.mapper.EnvioMapper;
import com.BlandiArruti.E_commerce.factura.mapper.FacturaMapper;
import com.BlandiArruti.E_commerce.pedido.dto.response.PedidoResponse;
import com.BlandiArruti.E_commerce.pedido.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ItemPedidoMapper.class, EnvioMapper.class, FacturaMapper.class})
public interface PedidoMapper {

    @Mapping(source = "uuid", target = "uuidPedido")
    @Mapping(source = "estadoPedido", target = "estado")
    @Mapping(source = "items", target = "items")
    PedidoResponse toResponse(Pedido pedido);
}
