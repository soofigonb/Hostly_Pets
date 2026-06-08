package cl.duoc.hostly_pagos_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagoDTO {
    private Long id;

    @NotNull(message = "El ID de la reserva es obligatorio.")
    private Long idReserva;

    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto del pago debe ser mayor a cero.")
    private Double monto;

    @NotBlank(message = "El método de pago es obligatorio.")
    private String metodoPago;

    private String nombreEstado;
}