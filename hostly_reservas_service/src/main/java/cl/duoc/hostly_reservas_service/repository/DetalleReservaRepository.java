package cl.duoc.hostly_reservas_service.repository;

import cl.duoc.hostly_reservas_service.model.DetalleReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleReservaRepository extends JpaRepository<DetalleReserva, Integer> {
}
