package cl.duoc.hostly_propiedades_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Datos requeridos para crear o actualizar una propiedad")
@Data
public class PropiedadRequestDTO {

    @Schema(description = "ID del dueño/anfitrión de la propiedad", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El id del anfitrión es obligatorio.")
    private Long idAnfitrion;

    @Schema(description = "Título descriptivo de la propiedad", example = "Casa pet-friendly en Santiago", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El título de la propiedad es obligatorio.")
    private String titulo;

    @Schema(description = "Descripción detallada de la propiedad", example = "Amplia casa con jardín", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La descripción de la propiedad es obligatoria.")
    private String descripcion;

    @Schema(description = "Dirección física de la propiedad", example = "Av. Providencia 123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La dirección de la propiedad es obligatoria.")
    private String direccion;

    @Schema(description = "Ciudad donde se ubica la propiedad", example = "Santiago", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La ciudad de la propiedad es obligatoria.")
    private String ciudad;

    @Schema(description = "Precio por noche en pesos chilenos", example = "35000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio por noche es obligatorio.")
    @Min(value = 1, message = "El precio por noche debe ser mayor a 0.")
    private Double precioNoche;

    @Schema(description = "Indica si la propiedad cuenta con patio", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Debe indicar si la propiedad tiene patio.")
    private Boolean tienePatio;

    @Schema(description = "Costo adicional por incluir una mascota", example = "5000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El costo extra por mascota es obligatorio.")
    @Min(value = 0, message = "El costo extra por mascota no puede ser negativo.")
    private Double costoExtraMascota;

    @Schema(description = "Indica si la propiedad está disponible para reservar", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Debe indicar si la propiedad está disponible.")
    private Boolean disponible;

    @Schema(description = "ID del tipo de propiedad (ej: 1=Casa, 2=Departamento)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El tipo de propiedad es obligatorio.")
    private Long idTipoPropiedad;

    @Schema(description = "ID del tipo de mascota aceptada (ej: 1=Perro, 2=Gato)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El tipo de mascota es obligatorio.")
    private Long idTipoMascota;

    @Schema(description = "ID del tamaño de mascota aceptado (ej: 1=Pequeño, 2=Mediano)", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El tamaño de mascota es obligatorio.")
    private Long idTamanoMascota;
}