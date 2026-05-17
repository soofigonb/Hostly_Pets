package cl.duoc.hostly_pagos_service.dto;

import lombok.Data;

@Data
public class PagoDTO {
    private Long id;
    private Long idReserva;
    private Double monto;
    private String metodoPago;
    private String nombreEstado;
}