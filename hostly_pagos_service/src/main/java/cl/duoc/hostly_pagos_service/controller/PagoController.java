package cl.duoc.hostly_pagos_service.controller;

import cl.duoc.hostly_pagos_service.dto.PagoDTO;
import cl.duoc.hostly_pagos_service.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Tag(name = "Pagos", description = "Operaciones relacionadas con pagos de reservas")
@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @Operation(summary = "Registrar un pago", description = "Procesa un nuevo pago asociado a una reserva")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pago registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del pago inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PagoDTO> realizarPago(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del pago a registrar", required = true)
            @Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO respuesta = pagoService.procesarPago(pagoDTO);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los pagos", description = "Obtiene todos los pagos registrados en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PagoDTO>> listarTodos() {
        return ResponseEntity.ok(pagoService.obtenerTodos());
    }

    @Operation(summary = "Buscar pagos por reserva", description = "Obtiene todos los pagos asociados a una reserva específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<PagoDTO>> buscarPorReserva(
            @Parameter(description = "ID de la reserva", example = "1", required = true)
            @PathVariable Long idReserva) {
        List<PagoDTO> pagos = pagoService.obtenerPagosPorReserva(idReserva);
        return ResponseEntity.ok(pagos);
    }

    @Operation(summary = "Actualizar un pago", description = "Actualiza los datos de un pago existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> actualizarPago(
            @Parameter(description = "ID del pago a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del pago", required = true)
            @Valid @RequestBody PagoDTO pagoDTO) {
        PagoDTO pagoActualizado = pagoService.actualizarPago(id, pagoDTO);
        return ResponseEntity.ok(pagoActualizado);
    }

    @Operation(summary = "Eliminar un pago", description = "Elimina un pago del sistema según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(
            @Parameter(description = "ID del pago a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}