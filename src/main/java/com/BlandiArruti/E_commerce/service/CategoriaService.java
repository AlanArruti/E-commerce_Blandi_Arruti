package com.BlandiArruti.E_commerce.service;

import com.BlandiArruti.E_commerce.dto.request.CategoriaRequest;
import com.BlandiArruti.E_commerce.dto.response.CategoriaResponse;
import com.BlandiArruti.E_commerce.entity.Categoria;
import com.BlandiArruti.E_commerce.exception.ConflictoException;
import com.BlandiArruti.E_commerce.exception.DuplicadoException;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.mapper.CategoriaMapper;
import com.BlandiArruti.E_commerce.repository.CategoriaRepository;
import com.BlandiArruti.E_commerce.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaMapper categoriaMapper;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return categoriaMapper.toResponse(
                categoriaRepository.findById(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.categoria(id))
        );
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        if (categoriaRepository.findByNombre(request.nombre()).isPresent()) {
            throw new DuplicadoException("La categoría '" + request.nombre() + "' ya existe.");
        }
        Categoria categoria = categoriaMapper.toEntity(request);
        return categoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> EntidadNoEncontradaException.categoria(id));

        // Validar nombre único solo si cambió
        if (!categoria.getNombre().equals(request.nombre())
                && categoriaRepository.findByNombre(request.nombre()).isPresent()) {
            throw new DuplicadoException("La categoría '" + request.nombre() + "' ya existe.");
        }

        categoria.setNombre(request.nombre());
        return categoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw EntidadNoEncontradaException.categoria(id);
        }
        if (!productoRepository.findByCategoriaId(id).isEmpty()) {
            throw ConflictoException.categoriaConProductos(id);
        }
        categoriaRepository.deleteById(id);
    }
}
