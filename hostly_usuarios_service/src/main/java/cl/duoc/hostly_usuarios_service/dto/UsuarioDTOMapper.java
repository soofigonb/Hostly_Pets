package cl.duoc.hostly_usuarios_service.dto;

//Importamos Component: sirve para que Spring reconozca esta clase como un componente manejado por el framework
import org.springframework.stereotype.Component;

//Importamos la excepción personalizada que se usa cuando no se encuentra un recurso 
import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;
import cl.duoc.hostly_usuarios_service.model.EstadoUsuario;
import cl.duoc.hostly_usuarios_service.model.Rol;
import cl.duoc.hostly_usuarios_service.model.Usuario;
import cl.duoc.hostly_usuarios_service.repository.EstadoUsuarioRepository;
import cl.duoc.hostly_usuarios_service.repository.RolRepository;
import lombok.RequiredArgsConstructor;

//Define esta clase como componente de Spring
@Component
@RequiredArgsConstructor
public class UsuarioDTOMapper {

    //Repositorio para obtener roles desde la base de datos
    private final RolRepository rolRepo;

    //Repositorio para obtener estados de usuario
    private final EstadoUsuarioRepository estadoUsuarioRepo;

    //Convierte una entidad Usuario en UsuarioDTO
    public UsuarioDTO toDTO(Usuario usuario){

        //Retorna null si el usuario es nulo
        if (usuario == null) {
            return null;
        }

        //Crea un nuevo DTO vacío
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        //Copia el ID del usuario desde la entidad hacia el DTO
        usuarioDTO.setIdUsuario(usuario.getIdUsuario()); 

        //Copia el nombre del usuario hacia el DTO
        usuarioDTO.setNombre(usuario.getNombre());

        //Copia el apellido del usuario hacia el DTO
        usuarioDTO.setApellido(usuario.getApellido());

        //Copia el email del usuario hacia el DTO
        usuarioDTO.setEmail(usuario.getEmail());

        //Copia el teléfono del usuario hacia el DTO
        usuarioDTO.setTelefono(usuario.getTelefono());

        //Copia la password del usuario hacia el DTO
        usuarioDTO.setPassword(usuario.getPassword());

        //Obtiene el ID del rol relacionado con el usuario y lo guarda en el DTO
        usuarioDTO.setIdRol(usuario.getRol().getIdRol());

        //Obtiene el ID del estado del usuario y lo guarda en el DTO
        usuarioDTO.setIdEstadoUsuario(usuario.getEstado().getIdEstadoUsuario());

        //Retorna el DTO ya lleno con los datos del usuario
        return usuarioDTO;

    }


    //Convierte un UsuarioDTO en entidad Usuario
    public Usuario toModel(UsuarioDTO usuarioDTO){

        //Retorna null si el DTO es nulo
        if (usuarioDTO == null) {
            return null;
        }


        //Busca el rol según el ID recibido
        Rol rol = rolRepo.findById(usuarioDTO.getIdRol()).orElseThrow(()-> new RecursoNoEncontradoException("Rol no encontrado"));

        //Busca el estado según el ID recibido
        EstadoUsuario estado = estadoUsuarioRepo.findById(usuarioDTO.getIdEstadoUsuario()).orElseThrow(()-> new RecursoNoEncontradoException("Estado no encontrado"));

        //Se crea una nueva entidad Usuario vacía
        Usuario usuario = new Usuario();

        //Copia el ID desde el DTO hacia la entidad Usuario
        usuario.setIdUsuario(usuarioDTO.getIdUsuario());

        //Copia el nombre desde el DTO hacia la entidad Usuario
        usuario.setNombre(usuarioDTO.getNombre());

        //Copia el apellido desde el DTO hacia la entidad Usuario
        usuario.setApellido(usuarioDTO.getApellido());

        //Copia el email desde el DTO hacia la entidad Usuario
        usuario.setEmail(usuarioDTO.getEmail());

        //Copia el teléfono desde el DTO hacia la entidad Usuario
        usuario.setTelefono(usuarioDTO.getTelefono());

        //Copia la password desde el DTO hacia la entidad Usuario
        usuario.setPassword(usuarioDTO.getPassword());

        //Asigna el rol encontrado en la base de datos al usuario
        usuario.setRol(rol);

        //Asigna el estado encontrado en la base de datos al usuario
        usuario.setEstado(estado);

        //Retorna la entidad Usuario lista para ser guardada o actualizada
        return usuario;
    }

}