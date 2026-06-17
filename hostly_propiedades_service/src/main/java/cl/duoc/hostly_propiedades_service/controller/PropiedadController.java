package cl.duoc.hostly_propiedades_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.hostly_propiedades_service.dto.PropiedadRequestDTO;
import cl.duoc.hostly_propiedades_service.dto.PropiedadResponseDTO;
import cl.duoc.hostly_propiedades_service.model.Propiedad;
import cl.duoc.hostly_propiedades_service.service.PropiedadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@Tag(name = "Propiedades", description = "Operaciones relacionadas con la gestión de propiedades para mascotas")
@RestController
@RequestMapping("/api/v1/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

    // Logger utilizado para registrar eventos y acciones del controlador
    private static final Logger logger =
            LoggerFactory.getLogger(PropiedadController.class);

    // Inyección del service de propiedades
    private final PropiedadService propiedadService;

    @Operation(summary = "Listar todas las propiedades", description = "Obtiene todas las propiedades registradas en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de propiedades obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Buscar propiedad por ID", description = "Obtiene la información completa de una propiedad por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedad encontrada"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PropiedadResponseDTO> obtenerPropiedadPorId(
            @Parameter(description = "ID único de la propiedad", example = "1", required = true)
            @PathVariable Long id) {

        // Log informativo
        logger.info("Solicitud GET para buscar propiedad con id {}", id);

        // Obtiene la propiedad desde el service
        Propiedad propiedad = propiedadService.obtenerPropiedadPorId(id);

        // Convierte la entidad en ResponseDTO
        PropiedadResponseDTO response = crearResponse(propiedad);

        // Retorna respuesta HTTP 200 OK
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear una propiedad", description = "Registra una nueva propiedad en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Propiedad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la propiedad inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("")
    public ResponseEntity<PropiedadResponseDTO> crearPropiedad(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la propiedad a crear", required = true)
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

    @Operation(summary = "Actualizar una propiedad", description = "Actualiza los datos de una propiedad existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PropiedadResponseDTO> actualizarPropiedad(
            @Parameter(description = "ID de la propiedad a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la propiedad", required = true)
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

    @Operation(summary = "Eliminar una propiedad", description = "Elimina una propiedad del sistema según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Propiedad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Propiedad no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPropiedad(
            @Parameter(description = "ID de la propiedad a eliminar", example = "1", required = true)
            @PathVariable Long id) {

        // Log de advertencia porque se eliminará información
        logger.warn("Solicitud DELETE para eliminar propiedad con id {}", id);

        // Elimina la propiedad desde el service
        propiedadService.eliminarPropiedad(id);

        // Retorna HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar propiedades por ciudad", description = "Obtiene todas las propiedades ubicadas en la ciudad indicada")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedades encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<PropiedadResponseDTO>>
            obtenerPropiedadesPorCiudad(
            @Parameter(description = "Nombre de la ciudad", example = "Santiago", required = true)
            @PathVariable String ciudad) {

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

    @Operation(summary = "Listar propiedades disponibles", description = "Obtiene todas las propiedades que se encuentran disponibles para reserva")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedades disponibles encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Buscar propiedades por anfitrión", description = "Obtiene todas las propiedades registradas por un anfitrión específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propiedades encontradas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/anfitrion/{idAnfitrion}")
    public ResponseEntity<List<PropiedadResponseDTO>>
            obtenerPropiedadesPorAnfitrion(
            @Parameter(description = "ID del anfitrión", example = "5", required = true)
            @PathVariable Long idAnfitrion) {

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