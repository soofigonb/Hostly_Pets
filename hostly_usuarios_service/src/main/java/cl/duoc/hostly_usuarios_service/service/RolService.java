package cl.duoc.hostly_usuarios_service.service;

//Logger: permite registrar mensajes y eventos de la aplicación
import org.slf4j.Logger;

//LoggerFactory: crea instancias de Logger
import org.slf4j.LoggerFactory;

//Service: indica que esta clase contiene lógica de negocio
import org.springframework.stereotype.Service;

//Importamos excepción
import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;

import cl.duoc.hostly_usuarios_service.model.Rol;
import cl.duoc.hostly_usuarios_service.repository.RolRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolService {

    // Repositorio para acceder a los datos de roles
    private final RolRepository rolRepo;

    // Logger para registrar eventos del servicio
    private static final Logger logger = LoggerFactory.getLogger(RolService.class);

    // Método para buscar un rol por su ID
    public Rol findById(Long idRol) {

        // Registra la búsqueda del rol
        logger.info("Buscando rol con ID: {}", idRol);

        // Valida que el ID no sea nulo
        if (idRol == null) {

            // Registra error de validación
            logger.warn("El ID del rol es nulo");

            throw new IllegalArgumentException("El id del rol no puede ser nulo");
        }

        // Busca el rol por ID
        Rol rol = rolRepo.findById(idRol)
                .orElseThrow(() -> {

                    // Registra que el rol no fue encontrado
                    logger.warn("Rol no encontrado con ID: {}", idRol);

                    return new RecursoNoEncontradoException("Rol no encontrado");
                });

        // Registra que el rol fue encontrado
        logger.info("Rol encontrado con ID: {}", idRol);

        // Retorna el rol encontrado
        return rol;
    }

    // Método para buscar un rol por su nombre
    public Rol findByNombreRol(String nombreRol) {

        // Registra la búsqueda del rol
        logger.info("Buscando rol con nombre: {}", nombreRol);

        // Valida que el nombre no sea nulo ni vacío
        if (nombreRol == null || nombreRol.isEmpty()) {

            // Registra error de validación
            logger.warn("El nombre del rol es nulo o vacío");

            throw new IllegalArgumentException("El nombre del rol no puede ser nulo o vacío");
        }

        // Busca el rol por nombre
        Rol rol = rolRepo.findByNombreRol(nombreRol)
                .orElseThrow(() -> {

                    logger.warn("Rol no encontrado con nombre: {}", nombreRol);

                    return new RecursoNoEncontradoException("Rol no encontrado");
                });

        // Registra que el rol fue encontrado
        logger.info("Rol encontrado: {}", nombreRol);

        // Retorna el rol encontrado
        return rol;
    }

}
