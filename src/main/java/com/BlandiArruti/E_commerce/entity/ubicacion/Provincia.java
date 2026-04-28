package com.BlandiArruti.E_commerce.entity.ubicacion;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"pais", "ciudades"})

@Entity
@Table(name = "provincias")
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_provincia")
    private Long id;
    @Column(name = "nombre_provincia",nullable = false,length = 50)
    private String nombre;

    @OneToMany(mappedBy = "provincia", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Ciudad> ciudades = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pais", nullable = false)
    private Pais pais;

}
