package cl.duoc.hostly_pagos_service.repository;

import cl.duoc.hostly_pagos_service.model.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    Optional<MetodoPago> findByNombreIgnoreCase(String nombre);
}
