package cl.duoc.hostly_propiedades_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_mascota")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoMascota {

    // Llave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_mascota")
    private Long idTipoMascota;

    // Nombre del tipo de mascota
    @Column(name = "nombre_tipo_mascota", nullable = false, unique = true)
    private String nombreTipoMascota;

}