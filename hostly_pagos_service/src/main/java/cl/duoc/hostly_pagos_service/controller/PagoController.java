package cl.duoc.hostly_pagos_service.controller;

import cl.duoc.hostly_pagos_service.dto.PagoDTO;
import cl.duoc.hostly_pagos_service.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    /**
     * Endpoint para registrar un nuevo pago.
     * URL: POST http://localhost:8083/api/v1/pagos
     */
    @PostMapping
    public ResponseEntity<PagoDTO> realizarPago(@Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO respuesta = pagoService.procesarPago(pagoDTO);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todos los pagos registrados.
     * URL: GET http://localhost:8083/api/pagos
     */
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    /**
     * Endpoint para buscar pagos asociados a una reserva específica.
     * URL: GET http://localhost:8083/api/pagos/reserva/{idReserva}
     */
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<PagoDTO>> buscarPorReserva(@PathVariable Long idReserva) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorReserva(idReserva);
        return ResponseEntity.ok(pagos);
    }

    /**
     * Endpoint para actualizar un pago existente.
     * URL: PUT http://localhost:8083/api/pagos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO pagoActualizado = pagoService.actualizarPago(id, pagoDTO);
        return ResponseEntity.ok(pagoActualizado);
    }

    /**
     * Endpoint para eliminar un pago.
     * URL: DELETE http://localhost:8083/api/pagos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}