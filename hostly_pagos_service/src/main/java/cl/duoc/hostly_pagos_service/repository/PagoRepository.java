package cl.duoc.hostly_pagos_service.repository;

import cl.duoc.hostly_pagos_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     #Busca todos los pagos asociados a una reserva específica.
     * Spring Data JPA genera la consulta SQL automáticamente basándose en el nombre del método.
     */
    List<Pago> findByIdReserva(Long idReserva);
}