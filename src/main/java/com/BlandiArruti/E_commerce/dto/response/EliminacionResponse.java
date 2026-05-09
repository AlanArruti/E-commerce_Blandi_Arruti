package com.BlandiArruti.E_commerce.dto.response;

import java.util.List;

public record EliminacionResponse(
        String mensaje,
        List<String> aviso
) {}
