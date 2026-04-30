package com.BlandiArruti.E_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"producto", "atributos"})

@Entity
@Table(name = "variantes")
public class Variante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
    @ElementCollection
    @CollectionTable(name = "variante_atributos", joinColumns = @JoinColumn(name = "id_variante"))
    @MapKeyColumn(name = "atributo_clave")
    @Column(name = "atributo_valor")
    private Map<String, String> atributos;
    @Column(name = "stock", nullable = false)
    private Integer stock;
}
