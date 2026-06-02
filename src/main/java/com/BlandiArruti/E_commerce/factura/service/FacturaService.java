package com.BlandiArruti.E_commerce.factura.service;

import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.factura.dto.request.FacturaResponse;
import com.BlandiArruti.E_commerce.factura.mapper.FacturaMapper;
import com.BlandiArruti.E_commerce.factura.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;

    public List<FacturaResponse> listarTodas() {
        return facturaRepository.findAll().stream()
                .map(facturaMapper::toResponse)
                .toList();
    }

    public FacturaResponse buscarPorId(Long id) {
        return facturaMapper.toResponse(
                facturaRepository.findById(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.factura(id))
        );
    }
}
