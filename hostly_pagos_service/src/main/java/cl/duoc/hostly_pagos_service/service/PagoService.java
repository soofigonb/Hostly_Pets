package cl.duoc.hostly_pagos_service.service;

import cl.duoc.hostly_pagos_service.dto.PagoDTO;
import cl.duoc.hostly_pagos_service.dto.PagoMapper;
import cl.duoc.hostly_pagos_service.model.Pago;
import cl.duoc.hostly_pagos_service.model.EstadoPago;
import cl.duoc.hostly_pagos_service.repository.PagoRepository;
import cl.duoc.hostly_pagos_service.repository.EstadoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    /**
     * Procesa y guarda un nuevo pago
     */
    public PagoDTO procesarPago(PagoDTO dto) {
        logger.info("Iniciando proceso de pago para Reserva ID: {}", dto.getIdReserva());

        // Validar monto básico
        if (dto.getMonto() == null || dto.getMonto() <= 0) {
            logger.error("Intento de pago con monto inválido: {}", dto.getMonto());
            throw new RuntimeException("El monto del pago debe ser mayor a cero");
        }

        // Convertir DTO a Entidad
        Pago pago = pagoMapper.toEntity(dto);

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

        return pagoMapper.toDTO(guardado);
    }

    /**
     * Lista todos los pagos en formato DTO
     */
    public List<PagoDTO> obtenerTodos() {
        logger.info("Listando todos los pagos del sistema");
        return pagoRepo.findAll().stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca pagos filtrados por el ID de la reserva
     */
    public List<PagoDTO> obtenerPagosPorReserva(Long idReserva) {
        logger.info("Buscando pagos asociados a la Reserva ID: {}", idReserva);
        
        List<Pago> pagos = pagoRepo.findByIdReserva(idReserva);
        
        return pagos.stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());
    }
}