package cl.duoc.hostly_propiedades_service.repository;

import java.util.Optional;

// JpaRepository entrega operaciones CRUD listas para usar
import org.springframework.data.jpa.repository.JpaRepository;

// Marca esta interfaz como repository de Spring
import org.springframework.stereotype.Repository;

import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;

@Repository
public interface TipoPropiedadRepository extends JpaRepository<TipoPropiedad, Long> {

    // Busca un tipo de propiedad según su nombre
    // Optional se usa porque puede existir o no en la base de datos
    Optional<TipoPropiedad> findByNombreTipoPropiedad(String nombreTipoPropiedad);

}