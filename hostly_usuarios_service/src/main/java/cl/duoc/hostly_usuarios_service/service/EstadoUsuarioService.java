package cl.duoc.hostly_usuarios_service.service;

//Logger: permite registrar mensajes y eventos de la aplicación
import org.slf4j.Logger;

//LoggerFactory: crea instancias de Logger
import org.slf4j.LoggerFactory;

//Service: indica que esta clase contiene lógica de negocio
import org.springframework.stereotype.Service;

//Importamos excepción
import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;

import cl.duoc.hostly_usuarios_service.model.EstadoUsuario;
import cl.duoc.hostly_usuarios_service.repository.EstadoUsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoUsuarioService {

    // Repositorio para acceder a los datos de estados de usuario
    private final EstadoUsuarioRepository estadoUsuarioRepo;

    // Logger para registrar eventos del servicio
    private static final Logger logger = LoggerFactory.getLogger(EstadoUsuarioService.class);

    // Método para buscar un estado de usuario por su ID
    public EstadoUsuario findById(Long idEstadoUsuario) {

        // Registra la búsqueda del estado
        logger.info("Buscando estado de usuario con ID: {}", idEstadoUsuario);

        // Valida que el ID no sea nulo
        if (idEstadoUsuario == null) {

            // Registra error de validación
            logger.warn("El ID del estado es nulo");

            throw new IllegalArgumentException("El id del estado no puede ser nulo");
        }

        // Busca el estado por ID
        EstadoUsuario estado = estadoUsuarioRepo.findById(idEstadoUsuario)
                .orElseThrow(() -> {

                    // Registra que el estado no fue encontrado
                    logger.warn("Estado de usuario no encontrado con ID: {}", idEstadoUsuario);

                    return new RecursoNoEncontradoException("Estado de usuario no encontrado");
                });

        // Registra que el estado fue encontrado
        logger.info("Estado encontrado con ID: {}", idEstadoUsuario);

        // Retorna el estado encontrado
        return estado;
    }

    // Método para buscar un estado de usuario por su nombre
    public EstadoUsuario findByNombreEstado(String nombreEstado) {

        // Registra la búsqueda del estado
        logger.info("Buscando estado de usuario con nombre: {}", nombreEstado);

        // Valida que el nombre no sea nulo ni vacío
        if (nombreEstado == null || nombreEstado.isEmpty()) {

            // Registra error de validación
            logger.warn("El nombre del estado es nulo o vacío");

            throw new IllegalArgumentException("El nombre del estado no puede ser nulo o vacío");
        }

        // Busca el estado por nombre
        EstadoUsuario estado = estadoUsuarioRepo.findByNombreEstado(nombreEstado)
                .orElseThrow(() -> {

                    logger.warn("Estado de usuario no encontrado con nombre: {}", nombreEstado);

                    return new RecursoNoEncontradoException("Estado de usuario no encontrado");
                });

        // Registra que el estado fue encontrado
        logger.info("Estado encontrado: {}", nombreEstado);

        // Retorna el estado encontrado
        return estado;
    }
}