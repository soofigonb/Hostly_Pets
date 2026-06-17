package cl.duoc.hostly_pagos_service.client;

import cl.duoc.hostly_pagos_service.dto.ReservaDTO; 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hostly-reservas-service", url = "http://localhost:8080/api/v1/reservas")
public interface ReservaClient {

    // Feign nos traerá la reserva completa con su precio real
    @GetMapping("/{id}")
    ReservaDTO obtenerReservaPorId(@PathVariable("id") Long id);

    @org.springframework.web.bind.annotation.PutMapping("/{id}/confirmar")
    void confirmarReserva(@PathVariable("id") Long id);
}