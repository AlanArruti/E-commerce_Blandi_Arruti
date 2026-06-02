package com.BlandiArruti.E_commerce.auth.service;

import com.BlandiArruti.E_commerce.administrador.entity.Administrador;
import com.BlandiArruti.E_commerce.cliente.entity.Cliente;
import com.BlandiArruti.E_commerce.enums.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final Rol rol;

    // constructor private para forzar a usar los factory methods, no se puede instanciar directo
    private UsuarioDetails(Long id, String username, String password, Rol rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public static UsuarioDetails fromCliente(Cliente cliente) {
        return new UsuarioDetails(
                cliente.getId(),
                cliente.getEmail(),
                cliente.getContrasenia(),
                Rol.CLIENTE
        );
    }

    public static UsuarioDetails fromAdmin(Administrador admin) {
        return new UsuarioDetails(
                admin.getId(),
                admin.getUsername(),
                admin.getContrasenia(),
                Rol.ADMIN
        );
    }

    //estos no son de UserDetails pero lo vamos necesitar en el JwtUtil para armar el token

    public Long getId() { return id; }
    public Rol getRol() { return rol; }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    //sin logica de bloqueo/expiración por ahora
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
