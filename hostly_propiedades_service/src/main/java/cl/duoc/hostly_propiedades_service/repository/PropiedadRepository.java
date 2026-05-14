package cl.duoc.hostly_propiedades_service.repository;

import java.util.List;

// JpaRepository entrega métodos CRUD automáticos
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.hostly_propiedades_service.model.Propiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    // Busca todas las propiedades ubicadas en una ciudad específica
    // Devuelve una lista porque pueden existir muchas propiedades en la misma ciudad
    List<Propiedad> findByCiudad(String ciudad);

    // Busca propiedades según su disponibilidad
    List<Propiedad> findByDisponible(Boolean disponible);

    // Busca todas las propiedades pertenecientes a un anfitrión
    // El idAnfitrion viene desde usuarios-service
    List<Propiedad> findByIdAnfitrion(Long idAnfitrion);

    // Busca propiedades según el tipo de propiedad
    // Ejemplo: Casa, Departamento, Cabaña
    List<Propiedad> findByTipoPropiedadIdTipoPropiedad(Long idTipoPropiedad);

    // Busca propiedades según el tipo de mascota permitida
    // Ejemplo: Perro, Gato, Conejo
    List<Propiedad> findByTipoMascotaIdTipoMascota(Long idTipoMascota);

    // Busca propiedades según el tamaño de mascota permitido
    // Ejemplo: Pequeño, Mediano, Grande
    List<Propiedad> findByTamanoMascotaIdTamanoMascota(Long idTamanoMascota);

}