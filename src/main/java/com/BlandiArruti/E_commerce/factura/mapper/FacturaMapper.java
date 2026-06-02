package com.BlandiArruti.E_commerce.factura.mapper;

import com.BlandiArruti.E_commerce.factura.dto.request.FacturaResponse;
import com.BlandiArruti.E_commerce.factura.entity.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacturaMapper {

    @Mapping(source = "uuid", target = "uuidFactura")
    @Mapping(source = "tipoFactura", target = "tipo")
    @Mapping(source = "pedido.id", target = "idPedido")
    FacturaResponse toResponse(Factura factura);
}
