package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.request.ProductoRequest;
import com.BlandiArruti.E_commerce.dto.response.ProductoResponse;
import com.BlandiArruti.E_commerce.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoriaMapper.class, VarianteMapper.class})
public interface ProductoMapper {

    @Mapping(source = "id", target = "idProducto")
    @Mapping(source = "precio", target = "precioBase")
    ProductoResponse toResponse(Producto producto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "precioBase", target = "precio")
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "variantes", expression = "java(new java.util.ArrayList<>())")
    Producto toEntity(ProductoRequest request);
}
