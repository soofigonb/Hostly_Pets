package cl.duoc.hostly_propiedades_service.service;

import java.util.List;

// Importamos Logger para registrar eventos y errores
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import cl.duoc.hostly_propiedades_service.clients.UsuarioClient;
import cl.duoc.hostly_propiedades_service.dto.PropiedadRequestDTO;
import cl.duoc.hostly_propiedades_service.dto.UsuarioResponseDTO;
import cl.duoc.hostly_propiedades_service.model.Propiedad;
import cl.duoc.hostly_propiedades_service.model.TamanoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;
import cl.duoc.hostly_propiedades_service.repository.PropiedadRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

// Indica que esta clase contiene lógica de negocio
@Service

// Permite manejar transacciones automáticamente
@Transactional

// Lombok genera constructor automáticamente
@RequiredArgsConstructor
public class PropiedadService {

    // Logger del service
    private static final Logger logger =
            LoggerFactory.getLogger(PropiedadService.class);

    // Repository principal de propiedades
    private final PropiedadRepository propiedadRepository;

    // Services auxiliares
    private final TipoPropiedadService tipoPropiedadService;
    private final TipoMascotaService tipoMascotaService;
    private final TamanoMascotaService tamanoMascotaService;

    // Cliente Feign para conectarse con usuarios-service
    private final UsuarioClient usuarioClient;

    // Obtiene todas las propiedades registradas
    public List<Propiedad> obtenerPropiedades() {

        logger.info("Obteniendo listado de propiedades");

        return propiedadRepository.findAll();
    }

    // Busca una propiedad según su ID
    public Propiedad obtenerPropiedadPorId(Long id) {

        logger.info("Buscando propiedad con id {}", id);

        return propiedadRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Propiedad no encontrada con id {}", id);

                    return new IllegalArgumentException("Propiedad no encontrada");
                });
    }

    // Crea una nueva propiedad
    public Propiedad crearPropiedad(PropiedadRequestDTO propiedadDTO) {

        logger.info("Creando propiedad para el anfitrión {}",
                propiedadDTO.getIdAnfitrion());

        // Verifica si el anfitrión existe en usuarios-service
        UsuarioResponseDTO usuario =
                usuarioClient.obtenerUsuarioPorId(
                        propiedadDTO.getIdAnfitrion());

        // Si no existe, lanza excepción
        if (usuario == null) {

            logger.error("Anfitrión no encontrado con id {}",
                    propiedadDTO.getIdAnfitrion());

            throw new IllegalArgumentException("Anfitrión no encontrado");
        }

        logger.info("Anfitrión encontrado: {}", usuario.getNombre());

        // Busca tipo de propiedad
        TipoPropiedad tipoPropiedad =
                tipoPropiedadService.obtenerTipoPropiedadPorId(
                        propiedadDTO.getIdTipoPropiedad());

        // Busca tipo de mascota
        TipoMascota tipoMascota =
                tipoMascotaService.obtenerTipoMascotaPorId(
                        propiedadDTO.getIdTipoMascota());

        // Busca tamaño de mascota
        TamanoMascota tamanoMascota =
                tamanoMascotaService.obtenerTamanoMascotaPorId(
                        propiedadDTO.getIdTamanoMascota());

        // Crea nueva entidad Propiedad
        Propiedad propiedad = new Propiedad();

        // Asigna datos básicos
        propiedad.setIdAnfitrion(propiedadDTO.getIdAnfitrion());
        propiedad.setTitulo(propiedadDTO.getTitulo());
        propiedad.setDescripcion(propiedadDTO.getDescripcion());
        propiedad.setDireccion(propiedadDTO.getDireccion());
        propiedad.setCiudad(propiedadDTO.getCiudad());
        propiedad.setPrecioNoche(propiedadDTO.getPrecioNoche());
        propiedad.setTienePatio(propiedadDTO.getTienePatio());
        propiedad.setCostoExtraMascota(propiedadDTO.getCostoExtraMascota());
        propiedad.setDisponible(propiedadDTO.getDisponible());

        // Asigna relaciones
        propiedad.setTipoPropiedad(tipoPropiedad);
        propiedad.setTipoMascota(tipoMascota);
        propiedad.setTamanoMascota(tamanoMascota);

        logger.info("Propiedad creada correctamente: {}",
                propiedad.getTitulo());

        // Guarda en base de datos
        return propiedadRepository.save(propiedad);
    }

    // Actualiza una propiedad existente
    public Propiedad actualizarPropiedad(Long id,
                                         PropiedadRequestDTO propiedadDTO) {

        logger.info("Actualizando propiedad con id {}", id);

        // Busca propiedad existente
        Propiedad propiedad = obtenerPropiedadPorId(id);

        // Verifica anfitrión en usuarios-service
        UsuarioResponseDTO usuario =
                usuarioClient.obtenerUsuarioPorId(
                        propiedadDTO.getIdAnfitrion());

        if (usuario == null) {

            logger.error("Anfitrión no encontrado con id {}",
                    propiedadDTO.getIdAnfitrion());

            throw new IllegalArgumentException("Anfitrión no encontrado");
        }

        logger.info("Anfitrión encontrado: {}", usuario.getNombre());

        // Busca relaciones
        TipoPropiedad tipoPropiedad =
                tipoPropiedadService.obtenerTipoPropiedadPorId(
                        propiedadDTO.getIdTipoPropiedad());

        TipoMascota tipoMascota =
                tipoMascotaService.obtenerTipoMascotaPorId(
                        propiedadDTO.getIdTipoMascota());

        TamanoMascota tamanoMascota =
                tamanoMascotaService.obtenerTamanoMascotaPorId(
                        propiedadDTO.getIdTamanoMascota());

        // Actualiza datos
        propiedad.setIdAnfitrion(propiedadDTO.getIdAnfitrion());
        propiedad.setTitulo(propiedadDTO.getTitulo());
        propiedad.setDescripcion(propiedadDTO.getDescripcion());
        propiedad.setDireccion(propiedadDTO.getDireccion());
        propiedad.setCiudad(propiedadDTO.getCiudad());
        propiedad.setPrecioNoche(propiedadDTO.getPrecioNoche());
        propiedad.setTienePatio(propiedadDTO.getTienePatio());
        propiedad.setCostoExtraMascota(propiedadDTO.getCostoExtraMascota());
        propiedad.setDisponible(propiedadDTO.getDisponible());

        // Actualiza relaciones
        propiedad.setTipoPropiedad(tipoPropiedad);
        propiedad.setTipoMascota(tipoMascota);
        propiedad.setTamanoMascota(tamanoMascota);

        logger.info("Propiedad actualizada correctamente con id {}", id);

        // Guarda cambios
        return propiedadRepository.save(propiedad);
    }

    // Elimina una propiedad por ID
    public void eliminarPropiedad(Long id) {

        logger.warn("Eliminando propiedad con id {}", id);

        // Busca propiedad
        Propiedad propiedad = obtenerPropiedadPorId(id);

        // Elimina de la base de datos
        propiedadRepository.delete(propiedad);

        logger.info("Propiedad eliminada correctamente con id {}", id);
    }

    // Busca propiedades según ciudad
    public List<Propiedad> obtenerPropiedadesPorCiudad(String ciudad) {

        logger.info("Buscando propiedades en la ciudad {}", ciudad);

        return propiedadRepository.findByCiudad(ciudad);
    }

    // Busca propiedades disponibles
    public List<Propiedad> obtenerPropiedadesDisponibles() {

        logger.info("Buscando propiedades disponibles");

        return propiedadRepository.findByDisponible(true);
    }

    // Busca propiedades según anfitrión
    public List<Propiedad> obtenerPropiedadesPorAnfitrion(Long idAnfitrion) {

        logger.info("Buscando propiedades del anfitrión {}", idAnfitrion);

        return propiedadRepository.findByIdAnfitrion(idAnfitrion);
    }
}