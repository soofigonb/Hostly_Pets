package cl.duoc.hostly_propiedades_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

// DTO utilizado para devolver información de propiedades
@Data
@Builder
public class PropiedadResponseDTO {

    @NotNull(message = "El id de la propiedad es obligatorio.")
    private Long idPropiedad;

    @NotNull(message = "El id del anfitrión es obligatorio.")
    private Long idAnfitrion;

    @NotBlank(message = "El título es obligatorio.")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria.")
    private String descripcion;

    @NotBlank(message = "La dirección es obligatoria.")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria.")
    private String ciudad;

    @NotNull(message = "El precio por noche es obligatorio.")
    private Double precioNoche;

    @NotNull(message = "Debe indicar si tiene patio.")
    private Boolean tienePatio;

    @NotNull(message = "El costo extra por mascota es obligatorio.")
    private Double costoExtraMascota;

    @NotNull(message = "Debe indicar si está disponible.")
    private Boolean disponible;

    @NotNull(message = "El id del tipo de propiedad es obligatorio.")
    private Long idTipoPropiedad;

    @NotBlank(message = "El nombre del tipo de propiedad es obligatorio.")
    private String tipoPropiedad;

    @NotNull(message = "El id del tipo de mascota es obligatorio.")
    private Long idTipoMascota;

    @NotBlank(message = "El nombre del tipo de mascota es obligatorio.")
    private String tipoMascota;

    @NotNull(message = "El id del tamaño de mascota es obligatorio.")
    private Long idTamanoMascota;

    @NotBlank(message = "El nombre del tamaño de mascota es obligatorio.")
    private String tamanoMascota;

}