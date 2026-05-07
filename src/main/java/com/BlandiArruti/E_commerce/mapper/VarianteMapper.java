package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.request.VarianteRequest;
import com.BlandiArruti.E_commerce.dto.response.VarianteResponse;
import com.BlandiArruti.E_commerce.entity.Variante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VarianteMapper {

    @Mapping(source = "id", target = "idVariante")
    @Mapping(source = "producto.id", target = "idProducto")
    VarianteResponse toResponse(Variante variante);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    Variante toEntity(VarianteRequest request);
}
