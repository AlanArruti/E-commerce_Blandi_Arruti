package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Optional<Ciudad> findByNombre(String nombre);
}
