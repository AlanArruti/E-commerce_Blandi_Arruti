package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUuid(String uuid);

    Optional<Cliente> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}
