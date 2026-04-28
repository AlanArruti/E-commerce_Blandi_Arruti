package com.BlandiArruti.E_commerce.entity;
import com.BlandiArruti.E_commerce.enums.TipoFactura;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "Facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Pedido pedido;
    private TipoFactura tipoFactura;
    private Double precioTotal;
}
