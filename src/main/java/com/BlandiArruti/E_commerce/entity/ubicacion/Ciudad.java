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
@ToString(exclude = {"provincia", "direcciones"}) // excluyo relaciones para evitar loops infinitos

@Entity
@Table(name = "ciudades")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long id;

    @Column(name = "nombre_ciudad", nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_provincia", nullable = false)
    private Provincia provincia;

    @OneToMany(mappedBy = "ciudad", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Direccion> direcciones = new ArrayList<>();


}
