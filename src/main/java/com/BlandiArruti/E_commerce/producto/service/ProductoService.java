package com.BlandiArruti.E_commerce.producto.service;

import com.BlandiArruti.E_commerce.categoria.entity.Categoria;
import com.BlandiArruti.E_commerce.categoria.repository.CategoriaRepository;
import com.BlandiArruti.E_commerce.enums.OperacionStock;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.exception.StockInsuficienteException;
import com.BlandiArruti.E_commerce.producto.dto.request.ProductoRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.StockRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.VarianteRequest;
import com.BlandiArruti.E_commerce.producto.dto.response.ProductoResponse;
import com.BlandiArruti.E_commerce.producto.dto.response.VarianteResponse;
import com.BlandiArruti.E_commerce.producto.entity.Producto;
import com.BlandiArruti.E_commerce.producto.entity.Variante;
import com.BlandiArruti.E_commerce.producto.mapper.ProductoMapper;
import com.BlandiArruti.E_commerce.producto.mapper.VarianteMapper;
import com.BlandiArruti.E_commerce.producto.repository.ProductoRepository;
import com.BlandiArruti.E_commerce.producto.repository.VarianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VarianteRepository varianteRepository;
    private final ProductoMapper productoMapper;
    private final VarianteMapper varianteMapper;

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos(Long categoriaId, Double precioMin, Double precioMax, String search) {
        List<Producto> productos;
        boolean hasSearch = search != null && !search.isBlank();

        if (hasSearch && categoriaId != null) {
            productos = productoRepository.findByActivoTrueAndNombreContainingIgnoreCaseAndCategoriaId(search.trim(), categoriaId);
        } else if (hasSearch) {
            productos = productoRepository.findByActivoTrueAndNombreContainingIgnoreCase(search.trim());
        } else if (categoriaId != null) {
            productos = productoRepository.findByActivoTrueAndCategoriaId(categoriaId);
        } else {
            productos = productoRepository.findAllByActivoTrue();
        }

        return productos.stream()
                .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
                .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
                .map(productoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorId(Long id) {
        return productoMapper.toResponse(
                productoRepository.findByIdAndActivoTrue(id)
                        .orElseThrow(() -> EntidadNoEncontradaException.producto(id))
        );
    }

    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> EntidadNoEncontradaException.categoria(request.categoriaId()));

        Producto producto = productoMapper.toEntity(request);
        producto.setCategoria(categoria);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> EntidadNoEncontradaException.producto(id));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> EntidadNoEncontradaException.categoria(request.categoriaId()));

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precioBase());
        producto.setCategoria(categoria);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    public void eliminar(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> EntidadNoEncontradaException.producto(id));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<VarianteResponse> listarVariantes(Long idProducto) {
        if (productoRepository.findByIdAndActivoTrue(idProducto).isEmpty()) {
            throw EntidadNoEncontradaException.producto(idProducto);
        }
        return varianteRepository.findByProductoIdAndActivoTrue(idProducto).stream()
                .map(varianteMapper::toResponse)
                .toList();
    }

    public VarianteResponse agregarVariante(Long idProducto, VarianteRequest request) {
        Producto producto = productoRepository.findByIdAndActivoTrue(idProducto)
                .orElseThrow(() -> EntidadNoEncontradaException.producto(idProducto));

        Variante variante = varianteMapper.toEntity(request);
        variante.setProducto(producto);
        return varianteMapper.toResponse(varianteRepository.save(variante));
    }

    public VarianteResponse actualizarVariante(Long idProducto, Long idVariante, VarianteRequest request) {
        if (productoRepository.findByIdAndActivoTrue(idProducto).isEmpty()) {
            throw EntidadNoEncontradaException.producto(idProducto);
        }
        Variante variante = varianteRepository.findByIdAndActivoTrue(idVariante)
                .orElseThrow(() -> EntidadNoEncontradaException.variante(idVariante));

        variante.setAtributos(request.atributos());
        variante.setStock(request.stock());
        return varianteMapper.toResponse(varianteRepository.save(variante));
    }

    public void eliminarVariante(Long idProducto, Long idVariante) {
        if (productoRepository.findByIdAndActivoTrue(idProducto).isEmpty()) {
            throw EntidadNoEncontradaException.producto(idProducto);
        }
        Variante variante = varianteRepository.findByIdAndActivoTrue(idVariante)
                .orElseThrow(() -> EntidadNoEncontradaException.variante(idVariante));
        variante.setActivo(false);
        varianteRepository.save(variante);
    }

    public VarianteResponse ajustarStock(Long idProducto, Long idVariante, StockRequest request) {
        if (productoRepository.findByIdAndActivoTrue(idProducto).isEmpty()) {
            throw EntidadNoEncontradaException.producto(idProducto);
        }
        Variante variante = varianteRepository.findByIdAndActivoTrue(idVariante)
                .orElseThrow(() -> EntidadNoEncontradaException.variante(idVariante));

        if (request.operacion() == OperacionStock.AGREGAR) {
            variante.setStock(variante.getStock() + request.cantidad());
        } else {
            if (variante.getStock() < request.cantidad()) {
                throw StockInsuficienteException.stockInsuficiente(
                        idVariante, variante.getStock(), request.cantidad());
            }
            variante.setStock(variante.getStock() - request.cantidad());
        }

        return varianteMapper.toResponse(varianteRepository.save(variante));
    }
}
