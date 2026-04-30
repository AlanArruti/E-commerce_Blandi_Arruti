package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Optional<Envio> findByPedidoId(Long idPedido);
}
