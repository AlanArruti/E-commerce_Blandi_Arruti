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
@ToString(exclude = {"provincias"})

@Entity
@Table(name = "paises")

public class Pais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private Long id;
    @Column(name = "nombre_pais", nullable = false,length = 50)
    private String nombre;

    @OneToMany(mappedBy = "pais", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Provincia> provincias = new ArrayList<>();
}
