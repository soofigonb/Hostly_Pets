package cl.duoc.hostly_usuarios_service.repository;

//Importamos Optional para evitar retornos null
import java.util.Optional;

//JpaRepository proporciona métodos CRUD automáticamente
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.hostly_usuarios_service.model.Usuario;

//Define esta interfaz como repositorio de Spring
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

    //Busca un usuario por email
    Optional<Usuario> findByEmail(String email);

    //Verifica si existe un usuario con ese email
    boolean existsByEmail(String email);

}