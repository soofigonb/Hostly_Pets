package cl.duoc.hostly_reservas_service.repository;

import cl.duoc.hostly_reservas_service.model.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {
}