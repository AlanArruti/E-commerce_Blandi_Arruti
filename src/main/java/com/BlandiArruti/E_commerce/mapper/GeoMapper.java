package com.BlandiArruti.E_commerce.mapper;

import com.BlandiArruti.E_commerce.dto.response.CiudadResponse;
import com.BlandiArruti.E_commerce.dto.response.PaisResponse;
import com.BlandiArruti.E_commerce.dto.response.ProvinciaResponse;
import com.BlandiArruti.E_commerce.entity.Ciudad;
import com.BlandiArruti.E_commerce.entity.Pais;
import com.BlandiArruti.E_commerce.entity.Provincia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GeoMapper {

    @Mapping(source = "id", target = "idPais")
    @Mapping(source = "nombre", target = "nombrePais")
    PaisResponse toResponse(Pais pais);

    @Mapping(source = "id", target = "idProvincia")
    @Mapping(source = "nombre", target = "nombreProvincia")
    ProvinciaResponse toResponse(Provincia provincia);

    @Mapping(source = "id", target = "idCiudad")
    @Mapping(source = "nombre", target = "nombreCiudad")
    CiudadResponse toResponse(Ciudad ciudad);
}
