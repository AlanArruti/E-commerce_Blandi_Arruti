package com.BlandiArruti.E_commerce.auth.controller;

import com.BlandiArruti.E_commerce.auth.dto.request.LoginRequest;
import com.BlandiArruti.E_commerce.auth.dto.response.TokenResponse;
import com.BlandiArruti.E_commerce.auth.service.AuthService;
import com.BlandiArruti.E_commerce.cliente.dto.request.ClienteRequest;
import com.BlandiArruti.E_commerce.cliente.dto.response.ClienteResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ClienteResponse> registrar(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }
}
