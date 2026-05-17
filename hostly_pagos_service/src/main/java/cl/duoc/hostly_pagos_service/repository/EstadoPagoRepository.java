package cl.duoc.hostly_pagos_service.repository;

import cl.duoc.hostly_pagos_service.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoPagoRepository extends JpaRepository<EstadoPago, Long> {
}