package cl.duoc.hostly_reservas_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.hostly_reservas_service.model.Reserva;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    // Busca las reservas activas de una propiedad para evitar overbooking
    List<Reserva> findByIdPropiedadAndEstadoIdNot(Long idPropiedad, Long estadoId);
}
