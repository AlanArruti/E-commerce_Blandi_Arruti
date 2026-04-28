package com.BlandiArruti.E_commerce.entity;
import com.BlandiArruti.E_commerce.enums.TipoFactura;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"pedido"})

@Entity
@Table(name = "facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_factura", nullable = false)
    private TipoFactura tipoFactura;
    @Column(name = "precio_total", nullable = false)
    private Double precioTotal;
}
