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
@Table(name = "Provincias")

public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_provincia")
    private Long id;
    @Column(name = "nombre_provincia",nullable = false,length = 50)
    private String nombre;
    private Pais pais;

}
