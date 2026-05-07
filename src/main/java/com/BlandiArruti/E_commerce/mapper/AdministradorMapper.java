package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.request.AdministradorRequest;
import com.BlandiArruti.E_commerce.dto.response.AdministradorResponse;
import com.BlandiArruti.E_commerce.entity.Administrador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdministradorMapper {

    @Mapping(source = "id", target = "idAdministrador")
    AdministradorResponse toResponse(Administrador administrador);

    @Mapping(target = "id", ignore = true)
    Administrador toEntity(AdministradorRequest request);
}
