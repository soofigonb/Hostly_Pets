package cl.duoc.hostly_reservas_service.services;

import cl.duoc.hostly_reservas_service.client.PropiedadClient;
import cl.duoc.hostly_reservas_service.client.UsuarioClient;
import cl.duoc.hostly_reservas_service.dto.ReservaDTO;
import cl.duoc.hostly_reservas_service.dto.PropiedadDTO;
import cl.duoc.hostly_reservas_service.dto.ReservaMapper;
import cl.duoc.hostly_reservas_service.exceptions.ResourceNotFoundException;
import cl.duoc.hostly_reservas_service.model.Reserva;
import cl.duoc.hostly_reservas_service.model.DetalleReserva;
import cl.duoc.hostly_reservas_service.model.EstadoReserva;
import java.math.BigDecimal;
import cl.duoc.hostly_reservas_service.repository.ReservaRepository;
import cl.duoc.hostly_reservas_service.repository.EstadoReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    // Repositorios e inyecciones 
    private final ReservaRepository reservaRepo;
    private final EstadoReservaRepository estadoRepo;
    private final ReservaMapper reservaMapper;
    
    // NUEVOS CLIENTES OPENFEIGN
    private final PropiedadClient propiedadClient;
    private final UsuarioClient usuarioClient;
    
    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    /// Crea una nueva reserva validando que existan el usuario y la propiedad
     
    public ReservaDTO crearReserva(ReservaDTO dto) {
        logger.info("Iniciando creación de reserva para Usuario ID: {} en Propiedad ID: {}", 
                    dto.getIdUsuario(), dto.getIdPropiedad());

        // VALIDACIONES HTTP CON MICROSERVICIOS (OPENFEIGN)
        
        // A. Validar si el usuario existe en el microservicio de usuarios
        try {
            logger.info("Consultando existencia del Usuario ID: {} en módulo Usuarios...", dto.getIdUsuario());
            usuarioClient.obtenerUsuarioPorId(dto.getIdUsuario());
        } catch (Exception e) {
            logger.error("Error Feign: El usuario con ID {} no existe o el módulo está caído", dto.getIdUsuario());
            throw new ResourceNotFoundException("No se puede crear la reserva: El usuario con ID " + dto.getIdUsuario() + " no existe.");
        }

        // B. Validar si la propiedad existe en el microservicio de propiedades 
        PropiedadDTO propiedadValida = null;
        try {
            logger.info("Consultando existencia de la Propiedad ID: {} en módulo Propiedades...", dto.getIdPropiedad());
            propiedadValida = propiedadClient.obtenerPropiedadPorId(dto.getIdPropiedad());
        } catch (Exception e) {
            logger.error("Error Feign: La propiedad con ID {} no existe o el módulo está caído", dto.getIdPropiedad());
            throw new ResourceNotFoundException("No se puede crear la reserva: La propiedad con ID " + dto.getIdPropiedad() + " no existe.");
        }

        if (propiedadValida != null && propiedadValida.getDisponible() != null && !propiedadValida.getDisponible()) {
            logger.error("La propiedad con ID {} no está disponible", dto.getIdPropiedad());
            throw new RuntimeException("No se puede crear la reserva: La propiedad no está actualmente disponible para arriendo.");
        }

        if (dto.getFechaInicio() != null && dto.getFechaInicio().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de inicio de la reserva no puede ser en el pasado.");
        }

        List<Reserva> reservasActivas = reservaRepo.findByIdPropiedadAndEstadoIdNot(dto.getIdPropiedad(), 3L);
        for (Reserva r : reservasActivas) {
            if (!dto.getFechaInicio().isAfter(r.getFechaFin()) && !dto.getFechaFin().isBefore(r.getFechaInicio())) {
                logger.error("Overbooking detectado en propiedad {} para las fechas {} a {}", dto.getIdPropiedad(), dto.getFechaInicio(), dto.getFechaFin());
                throw new RuntimeException("La propiedad ya se encuentra reservada en las fechas seleccionadas.");
            }
        }

        // 1. Convertir DTO a entidad
        Reserva reserva = reservaMapper.toEntity(dto);

        // 2. Lógica de negocio: Cálculo de noches
        long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        if (noches <= 0) {
            logger.error("Error en fechas: La reserva debe durar al menos 1 noche");
            throw new RuntimeException("La fecha de fin debe ser posterior a la de inicio");
        }

        // 3. Simulación y obtención de precios de la propiedad
        double precioNoche = 45000.0;
        double cargoPorMascota = 10000.0;
        if (propiedadValida != null) {
            if (propiedadValida.getPrecioNoche() != null) {
                precioNoche = propiedadValida.getPrecioNoche();
            }
            if (propiedadValida.getCostoExtraMascota() != null) {
                cargoPorMascota = propiedadValida.getCostoExtraMascota();
            }
        }

        double totalBase = noches * precioNoche;
        double totalMascota = reserva.getCantidadMascotas() * cargoPorMascota;
        double total = totalBase + totalMascota;
        reserva.setTotalReserva(total);
        logger.info("Cálculo finalizado: {} noches. Total: ${}", noches, total);

        // Crear e inyectar DetalleReserva
        DetalleReserva detalle = new DetalleReserva();
        detalle.setMontoBase(BigDecimal.valueOf(totalBase));
        detalle.setMontoMascota(BigDecimal.valueOf(totalMascota));
        detalle.setTotal(BigDecimal.valueOf(total));
        detalle.setReserva(reserva);
        reserva.setDetalle(detalle);

        // 4. Asignar Estado inicial PENDIENTE 
        EstadoReserva estadoInicial = estadoRepo.findById(1L)
                .orElseThrow(() -> {
                    logger.error("No se encontró el estado PENDIENTE (ID 1) en la base de datos");
                    return new RuntimeException("Estado inicial no configurado en la BD");
                });
        reserva.setEstado(estadoInicial);

        // 5. Guardar en Supabase
        Reserva guardada = reservaRepo.save(reserva);
        logger.info("Reserva guardada exitosamente con ID: {}", guardada.getId());

        // 6. Retornar como DTO para el controller
        return reservaMapper.toDTO(guardada);
    }

   /// Busca una reserva por su ID y le inyecta los datos de los microservicios
    
    public ReservaDTO obtenerReservaPorId(Long id) {
        logger.info("Buscando Reserva por ID: {}", id);
        
        Reserva reserva = reservaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));
                
        ReservaDTO dto = reservaMapper.toDTO(reserva);
        
        // Adjuntamos la información extendida en tiempo real de forma segura
        enriquecerReservaConFeign(dto);
        
        return dto;
    }

   ///Lista todas las reservas del sistema con sus datos vinculados
     
    public List<ReservaDTO> obtenerTodas() {
        logger.info("Obteniendo listado completo de reservas");
        
        return reservaRepo.findAll().stream()
                .map(reservaMapper::toDTO)
                .peek(this::enriquecerReservaConFeign) 
                .collect(Collectors.toList());
    }

    // 7. Actualizar una reserva existente
    public ReservaDTO actualizarReserva(Long id, ReservaDTO dto) {
        logger.info("Actualizando reserva con ID: {}", id);

        Reserva reservaExistente = reservaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));

        // Validar si el usuario o la propiedad han cambiado y si existen remotamente
        if (!reservaExistente.getIdUsuario().equals(dto.getIdUsuario())) {
            try {
                usuarioClient.obtenerUsuarioPorId(dto.getIdUsuario());
                reservaExistente.setIdUsuario(dto.getIdUsuario());
            } catch (Exception e) {
                logger.error("Error Feign: El usuario con ID {} no existe o el módulo está caído", dto.getIdUsuario());
                throw new ResourceNotFoundException("No se puede actualizar la reserva: El usuario con ID " + dto.getIdUsuario() + " no existe.");
            }
        }

        PropiedadDTO propiedadValida = null;
        if (!reservaExistente.getIdPropiedad().equals(dto.getIdPropiedad())) {
            try {
                propiedadValida = propiedadClient.obtenerPropiedadPorId(dto.getIdPropiedad());
                reservaExistente.setIdPropiedad(dto.getIdPropiedad());
            } catch (Exception e) {
                logger.error("Error Feign: La propiedad con ID {} no existe o el módulo está caído", dto.getIdPropiedad());
                throw new ResourceNotFoundException("No se puede actualizar la reserva: La propiedad con ID " + dto.getIdPropiedad() + " no existe.");
            }
        } else {
            // Traer precios de la propiedad actual para recalcular
            try {
                propiedadValida = propiedadClient.obtenerPropiedadPorId(reservaExistente.getIdPropiedad());
            } catch (Exception e) {
                logger.warn("No se pudo obtener la propiedad vía Feign para recalcular precios: {}", e.getMessage());
            }
        }

        reservaExistente.setFechaInicio(dto.getFechaInicio());
        reservaExistente.setFechaFin(dto.getFechaFin());
        reservaExistente.setCantidadMascotas(dto.getCantidadMascotas());
        reservaExistente.setTipoMascota(dto.getTipoMascota());
        reservaExistente.setTamanoMascota(dto.getTamanoMascota());

        // Validar sobreventa al actualizar
        List<Reserva> reservasActivas = reservaRepo.findByIdPropiedadAndEstadoIdNot(reservaExistente.getIdPropiedad(), 3L);
        for (Reserva r : reservasActivas) {
            if (!r.getId().equals(id)) {
                if (!reservaExistente.getFechaInicio().isAfter(r.getFechaFin()) && !reservaExistente.getFechaFin().isBefore(r.getFechaInicio())) {
                    throw new RuntimeException("La propiedad ya se encuentra reservada en las nuevas fechas seleccionadas.");
                }
            }
        }

        // Lógica de negocio: Cálculo de noches
        long noches = ChronoUnit.DAYS.between(reservaExistente.getFechaInicio(), reservaExistente.getFechaFin());
        if (noches <= 0) {
            logger.error("Error en fechas: La reserva debe durar al menos 1 noche");
            throw new RuntimeException("La fecha de fin debe ser posterior a la de inicio");
        }

        // Obtener precios dinámicos de la propiedad
        double precioNoche = 45000.0;
        double cargoPorMascota = 10000.0;
        if (propiedadValida != null) {
            if (propiedadValida.getPrecioNoche() != null) {
                precioNoche = propiedadValida.getPrecioNoche();
            }
            if (propiedadValida.getCostoExtraMascota() != null) {
                cargoPorMascota = propiedadValida.getCostoExtraMascota();
            }
        }

        double totalBase = noches * precioNoche;
        double totalMascota = reservaExistente.getCantidadMascotas() * cargoPorMascota;
        double total = totalBase + totalMascota;
        reservaExistente.setTotalReserva(total);

        // Crear/Actualizar DetalleReserva
        DetalleReserva detalle = reservaExistente.getDetalle();
        if (detalle == null) {
            detalle = new DetalleReserva();
            detalle.setReserva(reservaExistente);
        }
        detalle.setMontoBase(BigDecimal.valueOf(totalBase));
        detalle.setMontoMascota(BigDecimal.valueOf(totalMascota));
        detalle.setTotal(BigDecimal.valueOf(total));
        reservaExistente.setDetalle(detalle);

        Reserva guardada = reservaRepo.save(reservaExistente);
        logger.info("Reserva ID: {} actualizada con éxito", guardada.getId());

        return reservaMapper.toDTO(guardada);
    }

    // 8. Eliminar una reserva (Borrado Lógico)
    public void eliminarReserva(Long id) {
        logger.warn("Cancelando reserva con ID: {}", id);
        Reserva reserva = reservaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));
        
        EstadoReserva estadoCancelada = estadoRepo.findById(3L)
                .orElseThrow(() -> new RuntimeException("Estado CANCELADA (ID 3) no configurado en la BD"));
                
        reserva.setEstado(estadoCancelada);
        reservaRepo.save(reserva);
        
        logger.info("Reserva con ID: {} cancelada exitosamente", id);
    }

    // 9. Confirmar una reserva
    public void confirmarReserva(Long id) {
        logger.info("Confirmando reserva con ID: {}", id);
        Reserva reserva = reservaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva con ID " + id + " no encontrada"));

        EstadoReserva estadoConfirmada = estadoRepo.findById(2L)
                .orElseThrow(() -> new RuntimeException("Estado CONFIRMADA (ID 2) no configurado en la BD"));

        reserva.setEstado(estadoConfirmada);
        reservaRepo.save(reserva);
        logger.info("Reserva con ID: {} confirmada exitosamente", id);
    }

   // Método auxiliar privado para rellenar los datos de los micros 
     
    private void enriquecerReservaConFeign(ReservaDTO dto) {
        // Trae datos del usuario de forma asíncrona/segura
        try {
            Object usuarioCompleto = usuarioClient.obtenerUsuarioPorId(dto.getIdUsuario());
            dto.setUsuario(usuarioCompleto);
        } catch (Exception e) {
            logger.warn("No se pudieron cargar los datos del usuario para la reserva ID: {}. Detalles: {}", dto.getId(), e.getMessage());
            dto.setUsuario(null); 
        }

        // Trae datos de la propiedad de forma segura
        try {
            Object propiedadCompleta = propiedadClient.obtenerPropiedadPorId(dto.getIdPropiedad());
            dto.setPropiedad(propiedadCompleta);
        } catch (Exception e) {
            logger.warn("No se pudieron cargar los datos de la propiedad para la reserva ID: {}. Detalles: {}", dto.getId(), e.getMessage());
            dto.setPropiedad(null);
        }
    }
}