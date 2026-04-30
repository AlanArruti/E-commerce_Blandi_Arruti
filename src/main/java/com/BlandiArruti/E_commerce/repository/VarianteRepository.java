package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Variante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VarianteRepository extends JpaRepository<Variante, Long> {

    List<Variante> findByProductoId(Long idProducto);
}
