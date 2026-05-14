package cl.duoc.hostly_reservas_service.dto;

import cl.duoc.hostly_reservas_service.model.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {


    public ReservaDTO toDTO(Reserva reserva) {
        if (reserva == null) return null;

        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setIdUsuario(reserva.getIdUsuario());
        dto.setIdPropiedad(reserva.getIdPropiedad());
        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaFin(reserva.getFechaFin());
        dto.setCantidadMascotas(reserva.getCantidadMascotas());
        dto.setTipoMascota(reserva.getTipoMascota());
        dto.setTamanoMascota(reserva.getTamanoMascota());
        dto.setTotalReserva(reserva.getTotalReserva());

        // Mapeamos solo el nombre del estado para que el JSON sea legible
        if (reserva.getEstado() != null) {
            dto.setNombreEstado(reserva.getEstado().getNombre());
        }

        return dto;
    }


    public Reserva toEntity(ReservaDTO dto) {
        if (dto == null) return null;

        Reserva reserva = new Reserva();
        // El ID no se setea aquí si es una creación nueva (lo genera la BD)
        reserva.setIdUsuario(dto.getIdUsuario());
        reserva.setIdPropiedad(dto.getIdPropiedad());
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setCantidadMascotas(dto.getCantidadMascotas());
        reserva.setTipoMascota(dto.getTipoMascota());
        reserva.setTamanoMascota(dto.getTamanoMascota());
        reserva.setTotalReserva(dto.getTotalReserva());

        return reserva;
    }
}