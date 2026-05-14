package cl.duoc.hostly_propiedades_service.repository;

import java.util.Optional;

// JpaRepository entrega métodos CRUD automáticos
import org.springframework.data.jpa.repository.JpaRepository;

// Indica que esta interfaz pertenece a la capa Repository
import org.springframework.stereotype.Repository;

import cl.duoc.hostly_propiedades_service.model.TipoMascota;

@Repository
public interface TipoMascotaRepository extends JpaRepository<TipoMascota, Long> {

    // Busca un tipo de mascota según su nombre
    // Ejemplo: Perro, Gato , Conejo
    Optional<TipoMascota> findByNombreTipoMascota(String nombreTipoMascota);

}