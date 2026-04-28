package com.BlandiArruti.E_commerce.entity.ubicacion;
import com.BlandiArruti.E_commerce.enums.EstadoEnvio;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.CustomEntityDirtinessStrategy;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "Envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id_envio")
    private Long id;
    private Direccion direccion;
    private EstadoEnvio estado;
    @Column(name = "fechaSalida", nullable = false)
    private LocalDate fechaSalida;
    @Column(name = "fechaLlegada", nullable = false)
    private LocalDate fechaLlegada;

}
