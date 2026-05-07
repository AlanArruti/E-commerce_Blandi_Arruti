package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.request.CategoriaRequest;
import com.BlandiArruti.E_commerce.dto.response.CategoriaResponse;
import com.BlandiArruti.E_commerce.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    @Mapping(source = "id", target = "idCategoria")
    CategoriaResponse toResponse(Categoria categoria);

    @Mapping(target = "id", ignore = true)
    Categoria toEntity(CategoriaRequest request);
}
