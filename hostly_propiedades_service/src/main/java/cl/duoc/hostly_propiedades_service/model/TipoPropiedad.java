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
@Table(name = "tipo_propiedad")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoPropiedad {

    // Llave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_propiedad")
    private Long idTipoPropiedad;

    // Nombre del tipo de propiedad
    @Column(name = "nombre_tipo_propiedad", nullable = false, unique = true)
    private String nombreTipoPropiedad;

}