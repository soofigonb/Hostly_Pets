package cl.duoc.hostly_reservas_service.dto;

import lombok.Data;

@Data
public class PropiedadDTO {
    private Long idPropiedad;
    private Long idAnfitrion;
    private String titulo;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private Double precioNoche;
    private Boolean tienePatio;
    private Double costoExtraMascota;
    private Boolean disponible;
}