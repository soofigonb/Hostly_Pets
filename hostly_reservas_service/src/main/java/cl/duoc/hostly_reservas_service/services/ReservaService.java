package cl.duoc.hostly_reservas_service.services;

import cl.duoc.hostly_reservas_service.dto.ReservaDTO;
import cl.duoc.hostly_reservas_service.dto.ReservaMapper;
import cl.duoc.hostly_reservas_service.model.Reserva;
import cl.duoc.hostly_reservas_service.model.EstadoReserva;
import cl.duoc.hostly_reservas_service.repository.ReservaRepository;
import cl.duoc.hostly_reservas_service.repository.EstadoReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.temporal.ChronoUnit;
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
    
    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    public ReservaDTO crearReserva(ReservaDTO dto) {
        logger.info("Iniciando creación de reserva para Usuario ID: {} en Propiedad ID: {}", 
                    dto.getIdUsuario(), dto.getIdPropiedad());

        // 1. Convertir DTO a Entidad
        Reserva reserva = reservaMapper.toEntity(dto);

        // 2. Lógica de Negocio: Cálculo de noches
        long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        if (noches <= 0) {
            logger.error("Error en fechas: La reserva debe durar al menos 1 noche");
            throw new RuntimeException("La fecha de fin debe ser posterior a la de inicio");
        }

        // 3. Simulación de Precios 
        double precioNoche = 45000.0; 
        double cargoPorMascota = 10000.0;
        
        double total = (noches * precioNoche) + (reserva.getCantidadMascotas() * cargoPorMascota);
        reserva.setTotalReserva(total);
        logger.info("Cálculo finalizado: {} noches. Total: ${}", noches, total);

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

        // 6. Retornar como DTO para el Controller
        return reservaMapper.toDTO(guardada);
    }


    public List<ReservaDTO> obtenerTodas() {
        logger.info("Obteniendo listado completo de reservas");
        return reservaRepo.findAll().stream()
                .map(reservaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
