package cl.duoc.hostly_reservas_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(description = "Datos de una reserva de propiedad")
@Data
public class ReservaDTO {

    @Schema(description = "Identificador único de la reserva", example = "1")
    @JsonProperty("idReservas")
    private Long id;

    @Schema(description = "ID del usuario que realiza la reserva", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;

    @Schema(description = "ID de la propiedad a reservar", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de propiedad es obligatorio")
    private Long idPropiedad;

    @Schema(description = "Fecha de inicio de la reserva", example = "2025-01-15")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de la reserva", example = "2025-01-20")
    private LocalDate fechaFin;

    @Schema(description = "Cantidad de mascotas incluidas en la reserva", example = "2")
    @Positive(message = "Debe haber al menos 1 mascota")
    private Integer cantidadMascotas;

    @Schema(description = "Tipo de mascota (ej: Perro, Gato)", example = "Perro")
    private String tipoMascota;

    @Schema(description = "Tamaño de la mascota (ej: Pequeño, Mediano, Grande)", example = "Mediano")
    private String tamanoMascota;

    @Schema(description = "Monto total de la reserva calculado", example = "150000.0")
    private Double totalReserva;

    @Schema(description = "Estado actual de la reserva (ej: PENDIENTE, CONFIRMADA)", example = "PENDIENTE")
    private String nombreEstado;

    @Schema(description = "Detalle adicional de la reserva")
    private DetalleReservaDTO detalle;

    @Schema(description = "Información del usuario asociado")
    private Object usuario;

    @Schema(description = "Información de la propiedad asociada")
    private Object propiedad;
}