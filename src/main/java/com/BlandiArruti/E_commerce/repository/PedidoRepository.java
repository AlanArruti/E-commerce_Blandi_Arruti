package com.BlandiArruti.E_commerce.repository;

import com.BlandiArruti.E_commerce.entity.Pedido;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long idCliente);

    List<Pedido> findByEstadoPedido(EstadoPedido estado);

    List<Pedido> findByClienteIdAndEstadoPedido(Long idCliente, EstadoPedido estado);
}
