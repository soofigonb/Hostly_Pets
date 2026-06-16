package cl.duoc.hostly_propiedades_service.controller;

import java.util.List;
import java.util.stream.Collectors;

// Importamos Logger y LoggerFactory para generar logs del controlador
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ResponseEntity permite devolver respuestas HTTP personalizadas
import org.springframework.http.ResponseEntity;

// Importamos las anotaciones REST de Spring Boot
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importamos los DTO utilizados para recibir y devolver información
import cl.duoc.hostly_propiedades_service.dto.PropiedadRequestDTO;
import cl.duoc.hostly_propiedades_service.dto.PropiedadResponseDTO;

// Importamos la entidad Propiedad
import cl.duoc.hostly_propiedades_service.model.Propiedad;

// Importamos el service que contiene la lógica de negocio
import cl.duoc.hostly_propiedades_service.service.PropiedadService;

// Lombok genera automáticamente el constructor con atributos final
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

// Indica que esta clase será un controlador REST
@RestController

// Ruta base del controlador
@RequestMapping("/api/v1/propiedades")

// Genera constructor automático para inyección de dependencias
@RequiredArgsConstructor
public class PropiedadController {

    // Logger utilizado para registrar eventos y acciones del controlador
    private static final Logger logger =
            LoggerFactory.getLogger(PropiedadController.class);

    // Inyección del service de propiedades
    private final PropiedadService propiedadService;

    // Endpoint GET que lista todas las propiedades registradas
    @GetMapping("")
    public ResponseEntity<List<PropiedadResponseDTO>> obtenerPropiedades() {

        // Log informativo
        logger.info("Solicitud GET para listar propiedades");

        // Convierte la lista de entidades en ResponseDTO
        List<PropiedadResponseDTO> response = propiedadService.obtenerPropiedades()
                .stream()
                .map(this::crearResponse)
                .collect(Collectors.toList());

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Endpoint GET que busca una propiedad por ID
    @GetMapping("/{id}")
    public ResponseEntity<PropiedadResponseDTO> obtenerPropiedadPorId(@PathVariable Long id) {

        // Log informativo
        logger.info("Solicitud GET para buscar propiedad con id {}", id);

        // Obtiene la propiedad desde el service
        Propiedad propiedad = propiedadService.obtenerPropiedadPorId(id);

        // Convierte la entidad en ResponseDTO
        PropiedadResponseDTO response = crearResponse(propiedad);

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Endpoint POST que crea una nueva propiedad
    @PostMapping("")
    public ResponseEntity<PropiedadResponseDTO> crearPropiedad(
            @Valid @RequestBody PropiedadRequestDTO propiedadDTO) {

        // Log informativo
        logger.info("Solicitud POST para crear propiedad");

        // Envía los datos al service para crear la propiedad
        Propiedad propiedadCreada = propiedadService.crearPropiedad(propiedadDTO);

        // Convierte la entidad creada en ResponseDTO
        PropiedadResponseDTO response = crearResponse(propiedadCreada);

        // Retorna respuesta HTTP 201 Created
        return ResponseEntity.status(201).body(response);
    }

    // Endpoint PUT que actualiza una propiedad existente
    @PutMapping("/{id}")
    public ResponseEntity<PropiedadResponseDTO> actualizarPropiedad(
            @PathVariable Long id,
            @Valid @RequestBody PropiedadRequestDTO propiedadDTO) {

        // Log informativo
        logger.info("Solicitud PUT para actualizar propiedad con id {}", id);

        // Actualiza la propiedad desde el service
        Propiedad propiedadActualizada =
                propiedadService.actualizarPropiedad(id, propiedadDTO);

        // Convierte la entidad actualizada en ResponseDTO
        PropiedadResponseDTO response = crearResponse(propiedadActualizada);

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Endpoint DELETE que elimina una propiedad según su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPropiedad(@PathVariable Long id) {

        // Log de advertencia porque se eliminará información
        logger.warn("Solicitud DELETE para eliminar propiedad con id {}", id);

        // Elimina la propiedad desde el service
        propiedadService.eliminarPropiedad(id);

        // Retorna HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }

    // Endpoint GET que busca propiedades según ciudad
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<PropiedadResponseDTO>>
            obtenerPropiedadesPorCiudad(@PathVariable String ciudad) {

        // Log informativo
        logger.info("Solicitud GET para buscar propiedades en ciudad {}", ciudad);

        // Convierte la lista de propiedades en ResponseDTO
        List<PropiedadResponseDTO> response =
                propiedadService.obtenerPropiedadesPorCiudad(ciudad)
                        .stream()
                        .map(this::crearResponse)
                        .collect(Collectors.toList());

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Endpoint GET que obtiene propiedades disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<PropiedadResponseDTO>>
            obtenerPropiedadesDisponibles() {

        // Log informativo
        logger.info("Solicitud GET para buscar propiedades disponibles");

        // Convierte la lista de propiedades en ResponseDTO
        List<PropiedadResponseDTO> response =
                propiedadService.obtenerPropiedadesDisponibles()
                        .stream()
                        .map(this::crearResponse)
                        .collect(Collectors.toList());

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Endpoint GET que busca propiedades según anfitrión
    @GetMapping("/anfitrion/{idAnfitrion}")
    public ResponseEntity<List<PropiedadResponseDTO>>
            obtenerPropiedadesPorAnfitrion(@PathVariable Long idAnfitrion) {

        // Log informativo
        logger.info("Solicitud GET para buscar propiedades del anfitrión {}", idAnfitrion);

        // Convierte la lista de propiedades en ResponseDTO
        List<PropiedadResponseDTO> response =
                propiedadService.obtenerPropiedadesPorAnfitrion(idAnfitrion)
                        .stream()
                        .map(this::crearResponse)
                        .collect(Collectors.toList());

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    // Método privado que convierte una entidad Propiedad en ResponseDTO
    private PropiedadResponseDTO crearResponse(Propiedad propiedad) {

        // Builder utilizado para construir el DTO de respuesta
        return PropiedadResponseDTO.builder()

                .idPropiedad(propiedad.getIdPropiedad())
                .idAnfitrion(propiedad.getIdAnfitrion())
                .titulo(propiedad.getTitulo())
                .descripcion(propiedad.getDescripcion())
                .direccion(propiedad.getDireccion())
                .ciudad(propiedad.getCiudad())
                .precioNoche(propiedad.getPrecioNoche())
                .tienePatio(propiedad.getTienePatio())
                .costoExtraMascota(propiedad.getCostoExtraMascota())
                .disponible(propiedad.getDisponible())

                // Información del tipo de propiedad
                .idTipoPropiedad(propiedad.getTipoPropiedad().getIdTipoPropiedad())
                .tipoPropiedad(propiedad.getTipoPropiedad().getNombreTipoPropiedad())

                // Información del tipo de mascota
                .idTipoMascota(propiedad.getTipoMascota().getIdTipoMascota())
                .tipoMascota(propiedad.getTipoMascota().getNombreTipoMascota())

                // Información del tamaño de mascota
                .idTamanoMascota(propiedad.getTamanoMascota().getIdTamanoMascota())
                .tamanoMascota(propiedad.getTamanoMascota().getNombreTamanoMascota())

                // Construye el DTO final
                .build();
    }
}