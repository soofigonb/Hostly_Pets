package cl.duoc.hostly_usuarios_service.service;

//Logger: permite registrar mensajes y eventos de la aplicación
import org.slf4j.Logger;

//LoggerFactory: crea instancias de Logger
import org.slf4j.LoggerFactory;

//List: se usa para trabajar con listas de usuarios
import java.util.List;

//Service: indica que esta clase contiene lógica de negocio
import org.springframework.stereotype.Service;

//Importamos DTO y Mapper
import cl.duoc.hostly_usuarios_service.dto.UsuarioDTO;
import cl.duoc.hostly_usuarios_service.dto.UsuarioDTOMapper;

//Importamos excepciones:
import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;
import cl.duoc.hostly_usuarios_service.exceptions.RecursoYaExisteException;

import cl.duoc.hostly_usuarios_service.model.Usuario;
import cl.duoc.hostly_usuarios_service.repository.UsuarioRepository;

//Transactional: indica que los métodos trabajan dentro de una transacción con la base de datos
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    // Repositorio para acceder a los datos de usuarios
    private final UsuarioRepository usuarioRepo;

    // Mapper para convertir entre Usuario y UsuarioDTO
    private final UsuarioDTOMapper usuarioDTOMapper;

    // Logger para registrar eventos del servicio.
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    // Método que obtiene todos los usuarios registrados
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {

        // Registra la búsqueda de usuarios
        logger.info("Obteniendo lista de usuarios");

        // Busca todos los usuarios en la base de datos
        List<Usuario> usuarios = usuarioRepo.findAll();

        // Verifica si existen usuarios registrados
        if (usuarios.isEmpty()) {

            // Registra que no se encontraron usuarios
            logger.warn("No se encontraron usuarios registrados");

            throw new RecursoNoEncontradoException("No se encontraron usuarios.");
        }

        // Registra cantidad de usuarios encontrados
        logger.info("Usuarios encontrados: {}", usuarios.size());

        // Convierte la lista de Usuario a UsuarioDTO
        return usuarios.stream()
                .map(usuarioDTOMapper::toDTO)
                .toList();
    }

    // Método que obtiene un usuario según su ID
    public UsuarioDTO obtenerUsuarioPorId(Long idUsuario) {

        // Registra la búsqueda del usuario
        logger.info("Buscando usuario con ID: {}", idUsuario);

        // Busca el usuario por ID.
        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> {

                    // Registra que el usuario no fue encontrado
                    logger.warn("Usuario no encontrado con ID: {}", idUsuario);

                    return new RecursoNoEncontradoException("Usuario no encontrado");
                });

        // Registra que el usuario fue encontrado
        logger.info("Usuario encontrado con ID: {}", idUsuario);

        // Convierte y retorna el usuario
        return usuarioDTOMapper.toDTO(usuario);
    }

    // Método que obtiene un usuario según su email
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {

        // Registra la búsqueda por email
        logger.info("Buscando usuario con email: {}", email);

        // Busca un usuario por email
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> {

                    logger.warn("Usuario no encontrado con email: {}", email);

                    return new RecursoNoEncontradoException("Usuario no encontrado");
                });


        // Registra que el usuario fue encontrado
        logger.info("Usuario encontrado con email: {}", email);

        // Convierte y retorna el usuario
        return usuarioDTOMapper.toDTO(usuario);
    }

    // Método que agrega un nuevo usuario
    public UsuarioDTO agregarUsuario(UsuarioDTO usuarioDTO) {

        // Registra intento de creación
        logger.info("Registrando usuario con email: {}", usuarioDTO.getEmail());

        // Verifica si el email ya existe
        if (usuarioRepo.existsByEmail(usuarioDTO.getEmail())) {

            // Registra conflicto de email duplicado
            logger.warn("El email ya está registrado: {}", usuarioDTO.getEmail());

            throw new RecursoYaExisteException("Ya existe un usuario con ese email");
        }

        // Convierte el DTO en entidad Usuario
        Usuario usuario = usuarioDTOMapper.toModel(usuarioDTO);

        // Guarda el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        // Registra creación exitosa
        logger.info("Usuario registrado correctamente con ID: {}", usuarioGuardado.getIdUsuario());

        // Convierte y retorna el usuario guardado
        return usuarioDTOMapper.toDTO(usuarioGuardado);
    }

    // Método que actualiza un usuario existente
    public UsuarioDTO actualizarUsuario(Long idUsuario, UsuarioDTO usuarioDTOActualizado) {

        // Registra intento de actualización
        logger.info("Actualizando usuario con ID: {}", idUsuario);

        // Busca el usuario por ID
        Usuario usuarioExistente = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> {

                    // Registra que el usuario no fue encontrado
                    logger.warn("Usuario no encontrado con ID: {}", idUsuario);

                    return new RecursoNoEncontradoException("Usuario no encontrado");
                });

        // Verifica si el nuevo email ya está registrado
        if (!usuarioExistente.getEmail().equals(usuarioDTOActualizado.getEmail())
                && usuarioRepo.existsByEmail(usuarioDTOActualizado.getEmail())) {

            // Registra conflicto de email duplicado
            logger.warn("El email ya está registrado: {}", usuarioDTOActualizado.getEmail());

            throw new RecursoYaExisteException("Ya existe un usuario con ese email");
        }

        // Convierte el DTO actualizado en entidad Usuario
        Usuario usuarioActualizado = usuarioDTOMapper.toModel(usuarioDTOActualizado);

        // Mantiene el ID original del usuario
        usuarioActualizado.setIdUsuario(usuarioExistente.getIdUsuario());

        // Guarda los cambios
        Usuario usuarioGuardado = usuarioRepo.save(usuarioActualizado);

        // Registra actualización exitosa
        logger.info("Usuario actualizado correctamente con ID: {}", idUsuario);

        // Convierte y retorna el usuario actualizado
        return usuarioDTOMapper.toDTO(usuarioGuardado);
    }

    // Método que elimina un usuario según su ID
    public boolean eliminarUsuario(Long idUsuario) {

        // Registra intento de eliminación
        logger.info("Eliminando usuario con ID: {}", idUsuario);

        // Verifica si el usuario existe
        if (!usuarioRepo.existsById(idUsuario)) {

            // Registra que el usuario no fue encontrado
            logger.warn("Usuario no encontrado con ID: {}", idUsuario);

            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }

        // Elimina el usuario de la base de datos
        usuarioRepo.deleteById(idUsuario);

        // Registra eliminación exitosa
        logger.info("Usuario eliminado correctamente con ID: {}", idUsuario);

        // Retorna true si la eliminación fue exitosa
        return true;
    }

}