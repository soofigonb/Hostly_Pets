package cl.duoc.hostly_propiedades_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "propiedad")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Propiedad {

    // Llave primaria de la propiedad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propiedad")
    private Long idPropiedad;

    // ID del anfitrión que viene desde usuarios-service
    @Column(name = "id_anfitrion", nullable = false)
    private Long idAnfitrion;

    // Título de la publicación
    @Column(name = "titulo", nullable = false)
    private String titulo;

    // Descripción de la propiedad
    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    // Dirección de la propiedad
    @Column(name = "direccion", nullable = false)
    private String direccion;

    // Ciudad donde se ubica
    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    // Precio por noche
    @Column(name = "precio_noche", nullable = false)
    private Double precioNoche;

    // Indica si tiene patio
    @Column(name = "tiene_patio", nullable = false)
    private Boolean tienePatio;

    // Costo extra por mascota
    @Column(name = "costo_extra_mascota", nullable = false)
    private Double costoExtraMascota;

    // Indica si está disponible
    @Column(name = "disponible", nullable = false)
    private Boolean disponible;

    // Relación con tipo de propiedad
    @ManyToOne
    @JoinColumn(name = "id_tipo_propiedad", nullable = false)
    private TipoPropiedad tipoPropiedad;

    // Relación con tipo de mascota
    @ManyToOne
    @JoinColumn(name = "id_tipo_mascota", nullable = false)
    private TipoMascota tipoMascota;

    // Relación con tamaño de mascota
    @ManyToOne
    @JoinColumn(name = "id_tamano_mascota", nullable = false)
    private TamanoMascota tamanoMascota;

}
