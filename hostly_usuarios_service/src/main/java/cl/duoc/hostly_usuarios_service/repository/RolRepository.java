package cl.duoc.hostly_usuarios_service.repository;

//Importamos Optional para evitar retornos null
import java.util.Optional;

//JpaRepository proporciona métodos CRUD automáticamente
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.hostly_usuarios_service.model.Rol;

//Define esta interfaz como repositorio de Spring
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    //Busca un rol por nombre
    Optional<Rol> findByNombreRol(String nombreRol);

    //Verifica si existe un rol con ese nombre
    boolean existsByNombreRol(String nombreRol);

}
