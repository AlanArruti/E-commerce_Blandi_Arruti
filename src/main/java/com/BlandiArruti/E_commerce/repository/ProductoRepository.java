package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long idCategoria);

    List<Producto> findByNombreContainingIgnoreCase(String texto);
}
