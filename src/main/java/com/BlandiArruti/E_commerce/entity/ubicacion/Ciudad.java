package com.BlandiArruti.E_commerce.entity.ubicacion;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "Ciudades")

public class Ciudad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long id;
    @Column(name = "nombre_ciudad", nullable = false, length = 100)
    private String nombre;
    private Provincia provincia;
}
