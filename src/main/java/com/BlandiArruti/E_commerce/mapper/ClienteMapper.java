package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.request.ClienteRequest;
import com.BlandiArruti.E_commerce.dto.response.ClienteResponse;
import com.BlandiArruti.E_commerce.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DireccionMapper.class})
public interface ClienteMapper {

    @Mapping(source = "id", target = "idCliente")
    ClienteResponse toResponse(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "direcciones", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "pedidos", expression = "java(new java.util.ArrayList<>())")
    Cliente toEntity(ClienteRequest request);
}
