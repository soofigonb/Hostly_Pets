package cl.duoc.hostly_propiedades_service.repository;

import java.util.Optional;

// JpaRepository permite usar CRUD sin crear consultas manuales
import org.springframework.data.jpa.repository.JpaRepository;

// Marca esta interfaz como repository administrado por Spring
import org.springframework.stereotype.Repository;

import cl.duoc.hostly_propiedades_service.model.TamanoMascota;

@Repository
public interface TamanoMascotaRepository extends JpaRepository<TamanoMascota, Long> {

    // Busca un tamaño de mascota por su nombre
    // Ejemplo: Pequeño, Mediano o Grande
    Optional<TamanoMascota> findByNombreTamanoMascota(String nombreTamanoMascota);

}