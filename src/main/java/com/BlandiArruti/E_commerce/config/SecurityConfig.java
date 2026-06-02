package com.BlandiArruti.E_commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // TODO: inyectar JwtAuthFilter cuando esté creado
    // private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF (no es necesario en APIs REST stateless)
            .csrf(AbstractHttpConfigurer::disable)

            // Sin sesión HTTP — cada request se autentica con JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // ── Endpoints públicos ──────────────────────────────────────
                // Swagger / OpenAPI
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // Login y registro
                .requestMatchers("/api/v1/auth/**").permitAll()

                // Registro de cliente (cualquiera puede crear una cuenta)
                .requestMatchers(HttpMethod.POST, "/api/v1/cliente").permitAll()

                // Consulta pública de productos y categorías
                .requestMatchers(HttpMethod.GET, "/api/v1/producto/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/categoria/**").permitAll()

                // Consulta pública de geo (paises/provincias para formularios)
                .requestMatchers(HttpMethod.GET, "/api/v1/pais/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/provincia/**").permitAll()

                // ── Endpoints con roles (completar junto al compañero) ──────
                // .requestMatchers("/api/v1/administrador/**").hasRole("ADMIN")
                // .requestMatchers("/api/v1/cliente/**").hasAnyRole("ADMIN", "CLIENTE")
                // .requestMatchers("/api/v1/pedido/**").hasAnyRole("ADMIN", "CLIENTE")
                // .requestMatchers("/api/v1/envio/**").hasRole("ADMIN")

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            );

            // TODO: agregar el filtro JWT antes del UsernamePasswordAuthenticationFilter
            // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
