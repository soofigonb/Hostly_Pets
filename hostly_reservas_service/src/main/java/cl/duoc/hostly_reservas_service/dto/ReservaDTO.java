package cl.duoc.hostly_reservas_service.dto;

import java.time.LocalDate;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class ReservaDTO {
    private Long id;
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
    @NotNull(message = "El ID de propiedad es obligatorio")
    private Long idPropiedad;
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @Positive(message = "Debe haber al menos 1 mascota")
    private Integer cantidadMascotas;
    private String tipoMascota;
    private String tamanoMascota;
    private Double totalReserva;
    private String nombreEstado; 
}