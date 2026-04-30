package com.BlandiArruti.E_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"ciudad", "cliente"}) // evito loops en toString por relaciones bidireccionales

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion") // HAY QUE DARLE NOMBRE AL ID
    private Long id; // Long porque el YAML usa format: int64

    @Column(name = "nombre_calle", nullable = false, length = 100) // NO PUEDE SER NULLO NI MAYOR A 100CRT
    private String nombreCalle;

    @Column(name = "numero_calle", nullable = false)
    private Integer numeroCalle;

    // -------- RELACIONES --------
    // Muchas direcciones pertenecen a UNA ciudad.
    // Esta clase es el LADO DUEÑO: acá vive la FK "id_ciudad".
    // LAZY para no traer toda la cadena Ciudad -> Provincia -> Pais sin querer.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    // En Cliente, el inverso será: @OneToMany(mappedBy = "cliente")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}
