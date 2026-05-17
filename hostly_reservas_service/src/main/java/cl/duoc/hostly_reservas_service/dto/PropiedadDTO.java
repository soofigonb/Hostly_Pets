package cl.duoc.hostly_reservas_service.dto;

import lombok.Data;

@Data
public class PropiedadDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private Double precioPorNoche;
}