package com.BlandiArruti.E_commerce.entity;

import com.BlandiArruti.E_commerce.entity.ubicacion.Direccion;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table (name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String contrasenia;
    private List<Direccion> direcciones;
    private List<Pedido> pedidos;
}
