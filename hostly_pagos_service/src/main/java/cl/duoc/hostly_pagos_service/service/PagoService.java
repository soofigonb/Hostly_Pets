package cl.duoc.hostly_pagos_service.service;

import cl.duoc.hostly_pagos_service.client.ReservaClient;
import cl.duoc.hostly_pagos_service.dto.PagoDTO;
import cl.duoc.hostly_pagos_service.dto.PagoMapper;
import cl.duoc.hostly_pagos_service.dto.ReservaDTO;
import cl.duoc.hostly_pagos_service.model.Pago;
import cl.duoc.hostly_pagos_service.model.EstadoPago;
import cl.duoc.hostly_pagos_service.repository.PagoRepository;
import cl.duoc.hostly_pagos_service.repository.EstadoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cl.duoc.hostly_pagos_service.exceptions.ResourceNotFoundException;
import cl.duoc.hostly_pagos_service.model.MetodoPago;
import cl.duoc.hostly_pagos_service.repository.MetodoPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService {

    private final PagoRepository pagoRepo;
    private final EstadoPagoRepository estadoRepo;
    private final PagoMapper pagoMapper;
    private final ReservaClient reservaClient; 
    private final MetodoPagoRepository metodoPagoRepo;
    
    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    /// Procesa y guarda un nuevo pago validando la reserva con OpenFeign//
    public PagoDTO procesarPago(PagoDTO dto) {
        logger.info("Iniciando proceso de pago para Reserva ID: {}", dto.getIdReserva());

        if (dto.getIdReserva() == null) {
            logger.error("Intento de pago sin especificar ID de Reserva");
            throw new RuntimeException("El ID de la reserva es obligatorio para procesar el pago");
        }

        ReservaDTO reservaValida = null;

        // 2. Aduana de seguridad con OpenFeign
        try {
            logger.info("Viajando al microservicio de Reservas (8082) para validar ID: {}", dto.getIdReserva());
            reservaValida = reservaClient.obtenerReservaPorId(dto.getIdReserva());
        } catch (Exception e) {
            logger.error("Error al validar reserva vía Feign o la reserva no existe. Detalles: {}", e.getMessage());
            throw new RuntimeException("No se puede procesar el pago: La reserva con ID " + dto.getIdReserva() + " no existe o el servicio no responde.");
        }

        // 3. Validar si la reserva ya fue pagada exitosamente
        if (pagoRepo.existsByIdReservaAndEstadoId(dto.getIdReserva(), 2L)) {
            logger.error("Intento de pago duplicado para la Reserva ID: {}", dto.getIdReserva());
            throw new RuntimeException("La reserva ya se encuentra pagada.");
        }

        // 4. Viene el monto en el DTO de la reserva, lo usamos de forma automática
        if (reservaValida != null) {
            // Verificar que la reserva esté en estado PENDIENTE (asumiendo que es su nombreEstado o similar,
            // pero si no tenemos acceso seguro al nombre, al menos sabemos que no podemos cobrar una cancelada).
            // Lo ideal es verificar si getNombreEstado() es "Pendiente" o "PENDIENTE".
            if (reservaValida.getNombreEstado() != null && !reservaValida.getNombreEstado().equalsIgnoreCase("Pendiente")) {
                throw new RuntimeException("No se puede pagar una reserva que no esté en estado PENDIENTE. Estado actual: " + reservaValida.getNombreEstado());
            }

            if (reservaValida.getTotalReserva() != null) {
                logger.info("Monto de la reserva obtenido automáticamente: ${}", reservaValida.getTotalReserva());
                dto.setMonto(reservaValida.getTotalReserva());
            }
        }

        // Validar monto final por seguridad
        if (dto.getMonto() == null || dto.getMonto() <= 0) {
            logger.error("Monto final inválido o en cero: {}", dto.getMonto());
            throw new RuntimeException("El monto del pago debe ser mayor a cero");
        }

        // Convertir DTO a entidad
        Pago pago = pagoMapper.toEntity(dto);

        // Buscar y asignar el método de pago
        String nombreMetodo = dto.getMetodoPago();
        MetodoPago metodo = metodoPagoRepo.findByNombreIgnoreCase(nombreMetodo)
                .orElseThrow(() -> new RuntimeException("El método de pago '" + nombreMetodo + "' no está registrado o no es válido"));
        pago.setMetodoPago(metodo);

        // Asignar estado COMPLETADO (ID 2 en Supabase)
        EstadoPago estadoCompletado = estadoRepo.findById(2L)
                .orElseThrow(() -> {
                    logger.error("No se encontró el estado de pago COMPLETADO (ID 2) en la BD");
                    return new RuntimeException("Error de configuración: Estado de pago no encontrado");
                });
        
        pago.setEstado(estadoCompletado);

        // Guardar
        Pago guardado = pagoRepo.save(pago);
        logger.info("Pago guardado exitosamente. ID Transacción: {}", guardado.getId());

        // 5. Notificar a Reservas para cambiar el estado a CONFIRMADA
        try {
            reservaClient.confirmarReserva(dto.getIdReserva());
            logger.info("Reserva ID: {} confirmada exitosamente tras el pago", dto.getIdReserva());
        } catch (Exception e) {
            logger.error("Error al notificar confirmación a la reserva ID: {}. Detalles: {}", dto.getIdReserva(), e.getMessage());
            // No hacemos throw aquí para no revertir el pago exitoso, pero en un sistema real
            // se encolaría en un mensaje Kafka o RabbitMQ para reintentos.
        }

        return pagoMapper.toDTO(guardado);
    }

    /// Lista todos los pagos en formato DTO///
    
    public List<PagoDTO> obtenerTodos() {
        logger.info("Listando todos los pagos del sistema");
        return pagoRepo.findAll().stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /// Busca pagos filtrados por el ID de la reserva///
     
    
    public List<PagoDTO> obtenerPagosPorReserva(Long idReserva) {
        logger.info("Buscando pagos asociados a la Reserva ID: {}", idReserva);
        
        List<Pago> pagos = pagoRepo.findByIdReserva(idReserva);
        
        return pagos.stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /// Actualiza un pago existente///
    public PagoDTO actualizarPago(Long id, PagoDTO dto) {
        logger.info("Actualizando pago con ID: {}", id);

        Pago pagoExistente = pagoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + id + " no encontrado"));

        // Validar si la reserva cambió y existe remotamente
        if (!pagoExistente.getIdReserva().equals(dto.getIdReserva())) {
            try {
                logger.info("Validando nueva Reserva ID: {} vía OpenFeign...", dto.getIdReserva());
                reservaClient.obtenerReservaPorId(dto.getIdReserva());
                pagoExistente.setIdReserva(dto.getIdReserva());
            } catch (Exception e) {
                logger.error("Error al validar reserva vía Feign o la reserva no existe. Detalles: {}", e.getMessage());
                throw new RuntimeException("No se puede actualizar el pago: La reserva con ID " + dto.getIdReserva() + " no existe o el servicio no responde.");
            }
        }

        pagoExistente.setMonto(dto.getMonto());
        
        if (dto.getMetodoPago() != null) {
            MetodoPago metodo = metodoPagoRepo.findByNombreIgnoreCase(dto.getMetodoPago())
                    .orElseThrow(() -> new RuntimeException("El método de pago '" + dto.getMetodoPago() + "' no es válido"));
            pagoExistente.setMetodoPago(metodo);
        }

        Pago guardado = pagoRepo.save(pagoExistente);
        logger.info("Pago con ID: {} actualizado exitosamente", guardado.getId());

        return pagoMapper.toDTO(guardado);
    }

    /// Elimina un pago por su ID (Borrado Lógico)///
    public void eliminarPago(Long id) {
        logger.warn("Anulando pago con ID: {}", id);
        Pago pago = pagoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago con ID " + id + " no encontrado"));
        
        EstadoPago estadoAnulado = estadoRepo.findById(3L)
                .orElseThrow(() -> new RuntimeException("Estado de pago ANULADO (ID 3) no configurado en la BD"));

        pago.setEstado(estadoAnulado);
        pagoRepo.save(pago);
        logger.info("Pago con ID: {} anulado exitosamente", id);
    }
}