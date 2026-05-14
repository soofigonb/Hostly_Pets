package cl.duoc.hostly_usuarios_service.repository;

//Importamos Optional para evitar retornos null
import java.util.Optional;

//JpaRepository proporciona métodos CRUD automáticamente
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.hostly_usuarios_service.model.EstadoUsuario;

//Define esta interfaz como repositorio de Spring
@Repository
public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Long> {

    //Busca un estado por nombre
    Optional<EstadoUsuario> findByNombreEstado(String nombreEstado);

    //Verifica si existe un estado con ese nombre
    boolean existsByNombreEstado(String nombreEstado);

}