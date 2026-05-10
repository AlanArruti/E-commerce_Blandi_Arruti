package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Variante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VarianteRepository extends JpaRepository<Variante, Long> {

    Optional<Variante> findByUuid(String uuid);

    List<Variante> findByProductoId(Long idProducto);
}
