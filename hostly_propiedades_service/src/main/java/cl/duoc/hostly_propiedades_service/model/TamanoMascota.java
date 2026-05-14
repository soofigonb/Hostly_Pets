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
@Table(name = "tamano_mascota")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TamanoMascota {

    // Llave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tamano_mascota")
    private Long idTamanoMascota;

    // Nombre del tamaño permitido
    @Column(name = "nombre_tamano_mascota", nullable = false, unique = true)
    private String nombreTamanoMascota;

}