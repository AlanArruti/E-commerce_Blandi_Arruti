package com.BlandiArruti.E_commerce.envio.controller;

import com.BlandiArruti.E_commerce.envio.dto.request.EnvioRequest;
import com.BlandiArruti.E_commerce.envio.dto.request.EstadoEnvioRequest;
import com.BlandiArruti.E_commerce.envio.dto.response.EnvioResponse;
import com.BlandiArruti.E_commerce.envio.service.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/envio")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<EnvioResponse>> listarTodos() {
        return ResponseEntity.ok(envioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.buscarPorId(id));
    }

    @PostMapping("/pedido/{idPedido}")
    public ResponseEntity<EnvioResponse> crear(@PathVariable Long idPedido,
                                               @Valid @RequestBody EnvioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crear(idPedido, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstado(@PathVariable Long id,
                                                          @Valid @RequestBody EstadoEnvioRequest request) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
