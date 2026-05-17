package cl.duoc.hostly_pagos_service.dto;

import cl.duoc.hostly_pagos_service.model.Pago;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class PagoMapper {

    public PagoDTO toDTO(Pago pago) {
        if (pago == null) return null;
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setIdReserva(pago.getIdReserva());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        if (pago.getEstado() != null) {
            dto.setNombreEstado(pago.getEstado().getNombre());
        }
        return dto;
    }

    public Pago toEntity(PagoDTO dto) {
        if (dto == null) return null;
        Pago pago = new Pago();
        pago.setIdReserva(dto.getIdReserva());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(LocalDateTime.now());
        return pago;
    }
}
