package com.BlandiArruti.E_commerce.cliente.entity;

import com.BlandiArruti.E_commerce.geo.entity.Ciudad;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"ciudad", "cliente"})

@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    @PrePersist
    private void generarUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    @Column(name = "nombre_calle", nullable = false, length = 100)
    private String nombreCalle;

    @Column(name = "numero_calle", nullable = false)
    private Integer numeroCalle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}
