package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByUsername(String username);
}
