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
@Table(name = "Paises")

public class Pais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private Long id;
    @Column(name = "nombre_pais", nullable = false,length = 50)
    private String nombre;
}
