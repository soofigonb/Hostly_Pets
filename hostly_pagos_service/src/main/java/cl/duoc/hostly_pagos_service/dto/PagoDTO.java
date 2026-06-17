package cl.duoc.hostly_pagos_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(description = "Datos de un pago de reserva")
@Data
public class PagoDTO {

    @Schema(description = "Identificador único del pago", example = "1")
    private Long id;

    @Schema(description = "ID de la reserva asociada al pago", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la reserva es obligatorio.")
    private Long idReserva;

    @Schema(description = "Monto total del pago", example = "75000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto del pago debe ser mayor a cero.")
    private Double monto;

    @Schema(description = "Método de pago utilizado (ej: TRANSFERENCIA, TARJETA)", example = "TRANSFERENCIA", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El método de pago es obligatorio.")
    private String metodoPago;

    @Schema(description = "Estado actual del pago (ej: PENDIENTE, COMPLETADO)", example = "COMPLETADO")
    private String nombreEstado;
}