package cl.duoc.hostly_propiedades_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PropiedadRequestDTO {

    @NotNull(message = "El id del anfitrión es obligatorio.")
    private Long idAnfitrion;

    @NotBlank(message = "El título de la propiedad es obligatorio.")
    private String titulo;

    @NotBlank(message = "La descripción de la propiedad es obligatoria.")
    private String descripcion;

    @NotBlank(message = "La dirección de la propiedad es obligatoria.")
    private String direccion;

    @NotBlank(message = "La ciudad de la propiedad es obligatoria.")
    private String ciudad;

    @NotNull(message = "El precio por noche es obligatorio.")
    @Min(value = 1, message = "El precio por noche debe ser mayor a 0.")
    private Double precioNoche;

    @NotNull(message = "Debe indicar si la propiedad tiene patio.")
    private Boolean tienePatio;

    @NotNull(message = "El costo extra por mascota es obligatorio.")
    @Min(value = 0, message = "El costo extra por mascota no puede ser negativo.")
    private Double costoExtraMascota;

    @NotNull(message = "Debe indicar si la propiedad está disponible.")
    private Boolean disponible;

    @NotNull(message = "El tipo de propiedad es obligatorio.")
    private Long idTipoPropiedad;

    @NotNull(message = "El tipo de mascota es obligatorio.")
    private Long idTipoMascota;

    @NotNull(message = "El tamaño de mascota es obligatorio.")
    private Long idTamanoMascota;
}