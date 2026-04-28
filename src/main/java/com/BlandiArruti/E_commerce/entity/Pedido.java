package com.BlandiArruti.E_commerce.entity;

import com.BlandiArruti.E_commerce.entity.ubicacion.Envio;
import com.BlandiArruti.E_commerce.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Cliente cliente;
    private List<ItemPedido> items;
    private EstadoPedido estadoPedido;
    private Envio envio;
    private Factura factura;
}
