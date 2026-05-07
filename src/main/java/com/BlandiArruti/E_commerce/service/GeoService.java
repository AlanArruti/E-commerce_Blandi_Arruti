package com.BlandiArruti.E_commerce.service;

import com.BlandiArruti.E_commerce.dto.response.CiudadResponse;
import com.BlandiArruti.E_commerce.dto.response.PaisResponse;
import com.BlandiArruti.E_commerce.dto.response.ProvinciaResponse;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.mapper.GeoMapper;
import com.BlandiArruti.E_commerce.repository.PaisRepository;
import com.BlandiArruti.E_commerce.repository.ProvinciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeoService {

    private final PaisRepository paisRepository;
    private final ProvinciaRepository provinciaRepository;
    private final GeoMapper geoMapper;

    public List<PaisResponse> listarPaises() {
        return paisRepository.findAll().stream()
                .map(geoMapper::toResponse)
                .toList();
    }

    public List<ProvinciaResponse> listarProvinciasDePais(Long idPais) {
        if (!paisRepository.existsById(idPais)) {
            throw new EntidadNoEncontradaException("País con id " + idPais + " no encontrado.");
        }
        return provinciaRepository.findAll().stream()
                .filter(p -> p.getPais().getId().equals(idPais))
                .map(geoMapper::toResponse)
                .toList();
    }

    public List<CiudadResponse> listarCiudadesDeProvincia(Long idProvincia) {
        return provinciaRepository.findById(idProvincia)
                .map(provincia -> provincia.getCiudades().stream()
                        .map(geoMapper::toResponse)
                        .toList())
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Provincia con id " + idProvincia + " no encontrada."));
    }
}
