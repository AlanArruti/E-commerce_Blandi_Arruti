package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.response.DireccionResponse;
import com.BlandiArruti.E_commerce.entity.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {GeoMapper.class})
public interface DireccionMapper {

    @Mapping(source = "id", target = "idDireccion")
    DireccionResponse toResponse(Direccion direccion);
}
