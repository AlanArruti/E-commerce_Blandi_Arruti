package com.BlandiArruti.E_commerce.producto.service;

import com.BlandiArruti.E_commerce.categoria.entity.Categoria;
import com.BlandiArruti.E_commerce.categoria.repository.CategoriaRepository;
import com.BlandiArruti.E_commerce.enums.OperacionStock;
import com.BlandiArruti.E_commerce.exception.EntidadNoEncontradaException;
import com.BlandiArruti.E_commerce.exception.StockInsuficienteException;
import com.BlandiArruti.E_commerce.producto.dto.request.StockRequest;
import com.BlandiArruti.E_commerce.producto.dto.request.VarianteRequest;
import com.BlandiArruti.E_commerce.producto.entity.Producto;
import com.BlandiArruti.E_commerce.producto.entity.Variante;
import com.BlandiArruti.E_commerce.producto.mapper.ProductoMapper;
import com.BlandiArruti.E_commerce.producto.mapper.VarianteMapper;
import com.BlandiArruti.E_commerce.producto.repository.ProductoRepository;
import com.BlandiArruti.E_commerce.producto.repository.VarianteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock ProductoRepository productoRepository;
    @Mock CategoriaRepository categoriaRepository;
    @Mock VarianteRepository varianteRepository;
    @Mock ProductoMapper productoMapper;
    @Mock VarianteMapper varianteMapper;

    @InjectMocks
    ProductoService productoService;

    // ─── actualizarVariante — ownership ──────────────────────────────────────

    @Test
    void actualizarVariante_varianteDeOtroProducto_lanzaNotFound() {
        Producto producto1 = Producto.builder().id(1L).activo(true).build();
        Producto producto2 = Producto.builder().id(2L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto2).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto1));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));

        var request = new VarianteRequest(Map.of("talle", "M"), 5);

        assertThatThrownBy(() -> productoService.actualizarVariante(1L, 10L, request))
                .isInstanceOf(EntidadNoEncontradaException.class);
    }

    @Test
    void actualizarVariante_varianteDelProductoCorrecto_actualizaYRetorna() {
        Producto producto = Producto.builder().id(1L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto).stock(3).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));
        when(varianteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(varianteMapper.toResponse(any())).thenReturn(null);

        productoService.actualizarVariante(1L, 10L, new VarianteRequest(Map.of("color", "rojo"), 8));

        assertThat(variante.getStock()).isEqualTo(8);
        verify(varianteRepository).save(variante);
    }

    // ─── eliminarVariante — ownership ────────────────────────────────────────

    @Test
    void eliminarVariante_varianteDeOtroProducto_lanzaNotFound() {
        Producto producto1 = Producto.builder().id(1L).activo(true).build();
        Producto producto2 = Producto.builder().id(2L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto2).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto1));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));

        assertThatThrownBy(() -> productoService.eliminarVariante(1L, 10L))
                .isInstanceOf(EntidadNoEncontradaException.class);

        verify(varianteRepository, never()).save(any());
    }

    @Test
    void eliminarVariante_varianteDelProductoCorrecto_haceSoftDelete() {
        Producto producto = Producto.builder().id(1L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));
        when(varianteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        productoService.eliminarVariante(1L, 10L);

        assertThat(variante.isActivo()).isFalse();
    }

    // ─── ajustarStock — ownership + lógica ───────────────────────────────────

    @Test
    void ajustarStock_varianteDeOtroProducto_lanzaNotFound() {
        Producto producto1 = Producto.builder().id(1L).activo(true).build();
        Producto producto2 = Producto.builder().id(2L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto2).stock(5).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto1));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));

        assertThatThrownBy(() ->
                productoService.ajustarStock(1L, 10L, new StockRequest(3, OperacionStock.AGREGAR)))
                .isInstanceOf(EntidadNoEncontradaException.class);
    }

    @Test
    void ajustarStock_agregar_incrementaStock() {
        Producto producto = Producto.builder().id(1L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto).stock(5).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));
        when(varianteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(varianteMapper.toResponse(any())).thenReturn(null);

        productoService.ajustarStock(1L, 10L, new StockRequest(3, OperacionStock.AGREGAR));

        assertThat(variante.getStock()).isEqualTo(8);
    }

    @Test
    void ajustarStock_reducir_decrementaStock() {
        Producto producto = Producto.builder().id(1L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto).stock(10).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));
        when(varianteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(varianteMapper.toResponse(any())).thenReturn(null);

        productoService.ajustarStock(1L, 10L, new StockRequest(4, OperacionStock.REDUCIR));

        assertThat(variante.getStock()).isEqualTo(6);
    }

    @Test
    void ajustarStock_reducirMasQueStock_lanzaStockInsuficiente() {
        Producto producto = Producto.builder().id(1L).activo(true).build();
        Variante variante = Variante.builder().id(10L).activo(true).producto(producto).stock(2).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(varianteRepository.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(variante));

        assertThatThrownBy(() ->
                productoService.ajustarStock(1L, 10L, new StockRequest(5, OperacionStock.REDUCIR)))
                .isInstanceOf(StockInsuficienteException.class);
    }

    // ─── eliminar producto — soft delete ─────────────────────────────────────

    @Test
    void eliminar_productoExistente_haceSoftDelete() {
        Producto producto = Producto.builder().id(1L).activo(true).build();

        when(productoRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        productoService.eliminar(1L);

        assertThat(producto.isActivo()).isFalse();
    }

    @Test
    void eliminar_productoInexistente_lanzaNotFound() {
        when(productoRepository.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.eliminar(99L))
                .isInstanceOf(EntidadNoEncontradaException.class);
    }

    // ─── listarVariantes — producto inexistente ───────────────────────────────

    @Test
    void listarVariantes_productoInexistente_lanzaNotFound() {
        when(productoRepository.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.listarVariantes(99L))
                .isInstanceOf(EntidadNoEncontradaException.class);
    }
}
