package com.BlandiArruti.E_commerce.dto.response;

import com.BlandiArruti.E_commerce.enums.EstadoEnvio;

import java.time.LocalDate;

public record EnvioResponse(
        String uuidEnvio,
        DireccionResponse direccion,
        EstadoEnvio estado,
        LocalDate fechaSalida,
        LocalDate fechaLlegada
) {}
