package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.response.EnvioResponse;
import com.BlandiArruti.E_commerce.entity.Envio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DireccionMapper.class})
public interface EnvioMapper {

    @Mapping(source = "id", target = "idEnvio")
    EnvioResponse toResponse(Envio envio);
}
