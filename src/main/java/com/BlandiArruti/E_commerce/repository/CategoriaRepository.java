package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
}
