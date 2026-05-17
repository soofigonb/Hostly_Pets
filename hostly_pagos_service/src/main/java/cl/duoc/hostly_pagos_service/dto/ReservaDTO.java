package cl.duoc.hostly_pagos_service.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReservaDTO {
    private Long id;
    private Long idUsuario;
    private Long idPropiedad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantidadMascotas;
    private String tipoMascota;
    private String tamanoMascota;
    private Double totalReserva; 
    private String nombreEstado;
}