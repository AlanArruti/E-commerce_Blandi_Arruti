package com.BlandiArruti.E_commerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "administradores")

public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usernameAdm",nullable = false,length = 50)
    private String username;
    @Column(name = "contrasenia",nullable = false,length = 50)
    private String contrasenia;
}
