package cl.duoc.hostly_propiedades_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.hostly_propiedades_service.dto.PropiedadRequestDTO;
import cl.duoc.hostly_propiedades_service.model.Propiedad;
import cl.duoc.hostly_propiedades_service.model.TamanoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;
import cl.duoc.hostly_propiedades_service.repository.PropiedadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PropiedadService {

    private static final Logger logger = LoggerFactory.getLogger(PropiedadService.class);

    private final PropiedadRepository propiedadRepository;
    private final TipoPropiedadService tipoPropiedadService;
    private final TipoMascotaService tipoMascotaService;
    private final TamanoMascotaService tamanoMascotaService;

    // Lista todas las propiedades registradas
    public List<Propiedad> obtenerPropiedades() {

        logger.info("Obteniendo listado de propiedades");

        return propiedadRepository.findAll();
    }

    // Busca una propiedad por ID
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

        logger.info("Creando propiedad para el anfitrión {}", propiedadDTO.getIdAnfitrion());

        TipoPropiedad tipoPropiedad = tipoPropiedadService
                .obtenerTipoPropiedadPorId(propiedadDTO.getIdTipoPropiedad());

        TipoMascota tipoMascota = tipoMascotaService
                .obtenerTipoMascotaPorId(propiedadDTO.getIdTipoMascota());

        TamanoMascota tamanoMascota = tamanoMascotaService
                .obtenerTamanoMascotaPorId(propiedadDTO.getIdTamanoMascota());

        Propiedad propiedad = new Propiedad();

        propiedad.setIdAnfitrion(propiedadDTO.getIdAnfitrion());
        propiedad.setTitulo(propiedadDTO.getTitulo());
        propiedad.setDescripcion(propiedadDTO.getDescripcion());
        propiedad.setDireccion(propiedadDTO.getDireccion());
        propiedad.setCiudad(propiedadDTO.getCiudad());
        propiedad.setPrecioNoche(propiedadDTO.getPrecioNoche());
        propiedad.setTienePatio(propiedadDTO.getTienePatio());
        propiedad.setCostoExtraMascota(propiedadDTO.getCostoExtraMascota());
        propiedad.setDisponible(propiedadDTO.getDisponible());
        propiedad.setTipoPropiedad(tipoPropiedad);
        propiedad.setTipoMascota(tipoMascota);
        propiedad.setTamanoMascota(tamanoMascota);

        logger.info("Propiedad creada correctamente: {}", propiedad.getTitulo());

        return propiedadRepository.save(propiedad);
    }

    // Actualiza una propiedad existente
    public Propiedad actualizarPropiedad(Long id, PropiedadRequestDTO propiedadDTO) {

        logger.info("Actualizando propiedad con id {}", id);

        Propiedad propiedad = obtenerPropiedadPorId(id);

        TipoPropiedad tipoPropiedad = tipoPropiedadService
                .obtenerTipoPropiedadPorId(propiedadDTO.getIdTipoPropiedad());

        TipoMascota tipoMascota = tipoMascotaService
                .obtenerTipoMascotaPorId(propiedadDTO.getIdTipoMascota());

        TamanoMascota tamanoMascota = tamanoMascotaService
                .obtenerTamanoMascotaPorId(propiedadDTO.getIdTamanoMascota());

        propiedad.setIdAnfitrion(propiedadDTO.getIdAnfitrion());
        propiedad.setTitulo(propiedadDTO.getTitulo());
        propiedad.setDescripcion(propiedadDTO.getDescripcion());
        propiedad.setDireccion(propiedadDTO.getDireccion());
        propiedad.setCiudad(propiedadDTO.getCiudad());
        propiedad.setPrecioNoche(propiedadDTO.getPrecioNoche());
        propiedad.setTienePatio(propiedadDTO.getTienePatio());
        propiedad.setCostoExtraMascota(propiedadDTO.getCostoExtraMascota());
        propiedad.setDisponible(propiedadDTO.getDisponible());
        propiedad.setTipoPropiedad(tipoPropiedad);
        propiedad.setTipoMascota(tipoMascota);
        propiedad.setTamanoMascota(tamanoMascota);

        logger.info("Propiedad actualizada correctamente con id {}", id);

        return propiedadRepository.save(propiedad);
    }

    // Elimina una propiedad por ID
    public void eliminarPropiedad(Long id) {

        logger.warn("Eliminando propiedad con id {}", id);

        Propiedad propiedad = obtenerPropiedadPorId(id);

        propiedadRepository.delete(propiedad);

        logger.info("Propiedad eliminada correctamente con id {}", id);
    }

    // Busca propiedades por ciudad
    public List<Propiedad> obtenerPropiedadesPorCiudad(String ciudad) {

        logger.info("Buscando propiedades en la ciudad {}", ciudad);

        return propiedadRepository.findByCiudad(ciudad);
    }

    // Busca propiedades disponibles
    public List<Propiedad> obtenerPropiedadesDisponibles() {

        logger.info("Buscando propiedades disponibles");

        return propiedadRepository.findByDisponible(true);
    }

    // Busca propiedades por anfitrión
    public List<Propiedad> obtenerPropiedadesPorAnfitrion(Long idAnfitrion) {

        logger.info("Buscando propiedades del anfitrión {}", idAnfitrion);

        return propiedadRepository.findByIdAnfitrion(idAnfitrion);
    }
}
