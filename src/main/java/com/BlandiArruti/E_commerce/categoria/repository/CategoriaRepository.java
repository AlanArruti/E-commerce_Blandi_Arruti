package com.BlandiArruti.E_commerce.categoria.repository;

import com.BlandiArruti.E_commerce.categoria.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByUuid(String uuid);

    Optional<Categoria> findByNombre(String nombre);
}
