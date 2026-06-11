package com.BlandiArruti.E_commerce.producto.repository;

import com.BlandiArruti.E_commerce.producto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByUuid(String uuid);

    Optional<Producto> findByIdAndActivoTrue(Long id);

    List<Producto> findAllByActivoTrue();

    List<Producto> findByCategoriaId(Long idCategoria);

    List<Producto> findByActivoTrueAndCategoriaId(Long idCategoria);

    List<Producto> findByActivoTrueAndNombreContainingIgnoreCase(String texto);
}
