package com.BlandiArruti.E_commerce.config;

import com.BlandiArruti.E_commerce.auth.filter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(
                        "{\"status\":401,\"error\":\"No autorizado\",\"message\":\"" +
                        authException.getMessage() + "\"}"
                    );
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(
                        "{\"status\":403,\"error\":\"Acceso denegado\",\"message\":\"" +
                        accessDeniedException.getMessage() + "\"}"
                    );
                })
            )

            .authorizeHttpRequests(auth -> auth

                // ── Públicos ────────────────────────────────────────────────
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers("/api/v1/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/producto/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/paises/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/provincias/**").permitAll()

                // ── Solo ADMIN ───────────────────────────────────────────────
                .requestMatchers("/api/v1/administrador/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/envio/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST,   "/api/v1/producto/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/v1/producto/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/producto/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,  "/api/v1/producto/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST,   "/api/v1/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/v1/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole("ADMIN")

                // ── ADMIN o CLIENTE ──────────────────────────────────────────
                .requestMatchers("/api/v1/pedido/**").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers("/api/v1/cliente/**").hasAnyRole("ADMIN", "CLIENTE")

                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

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
