package com.BlandiArruti.E_commerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "El nombre no puede estar vacío.")
        @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
        String nombre
) {}
