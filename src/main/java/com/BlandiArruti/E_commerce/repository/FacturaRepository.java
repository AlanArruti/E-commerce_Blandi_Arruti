package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Factura;
import com.BlandiArruti.E_commerce.enums.TipoFactura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByPedidoId(Long idPedido);

    List<Factura> findByTipoFactura(TipoFactura tipo);
}
