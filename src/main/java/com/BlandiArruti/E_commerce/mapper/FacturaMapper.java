package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.response.FacturaResponse;
import com.BlandiArruti.E_commerce.entity.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacturaMapper {

    @Mapping(source = "id", target = "idFactura")
    @Mapping(source = "tipoFactura", target = "tipo")
    @Mapping(source = "pedido.id", target = "idPedido")
    FacturaResponse toResponse(Factura factura);
}
