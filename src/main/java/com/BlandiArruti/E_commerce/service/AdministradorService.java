package com.BlandiArruti.E_commerce.service;

import com.BlandiArruti.E_commerce.dto.request.AdministradorRequest;
import com.BlandiArruti.E_commerce.dto.response.AdministradorResponse;
import com.BlandiArruti.E_commerce.entity.Administrador;
import com.BlandiArruti.E_commerce.exception.DuplicadoException;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.mapper.AdministradorMapper;
import com.BlandiArruti.E_commerce.repository.AdministradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final AdministradorMapper administradorMapper;

    @Transactional(readOnly = true)
    public List<AdministradorResponse> listarTodos() {
        return administradorRepository.findAll().stream()
                .map(administradorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdministradorResponse buscarPorId(Long id) {
        return administradorMapper.toResponse(
                administradorRepository.findById(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.administrador(id))
        );
    }

    public AdministradorResponse crear(AdministradorRequest request) {
        if (administradorRepository.findByUsername(request.username()).isPresent()) {
            throw DuplicadoException.username(request.username());
        }
        Administrador admin = administradorMapper.toEntity(request);
        return administradorMapper.toResponse(administradorRepository.save(admin));
    }

    public void eliminar(Long id) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.administrador(id));
        administradorRepository.delete(admin);
    }
}
