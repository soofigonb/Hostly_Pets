package cl.duoc.hostly_reservas_service.dto;

import cl.duoc.hostly_reservas_service.model.Reserva;
import cl.duoc.hostly_reservas_service.model.DetalleReserva;
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

        // Mapear DetalleReserva
        if (reserva.getDetalle() != null) {
            DetalleReservaDTO detalleDTO = new DetalleReservaDTO();
            detalleDTO.setId(reserva.getDetalle().getId());
            detalleDTO.setMontoBase(reserva.getDetalle().getMontoBase());
            detalleDTO.setMontoMascota(reserva.getDetalle().getMontoMascota());
            detalleDTO.setTotal(reserva.getDetalle().getTotal());
            dto.setDetalle(detalleDTO);
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

        // Mapear DetalleReserva de vuelta
        if (dto.getDetalle() != null) {
            DetalleReserva det = new DetalleReserva();
            det.setId(dto.getDetalle().getId());
            det.setMontoBase(dto.getDetalle().getMontoBase());
            det.setMontoMascota(dto.getDetalle().getMontoMascota());
            det.setTotal(dto.getDetalle().getTotal());
            det.setReserva(reserva);
            reserva.setDetalle(det);
        }

        return reserva;
    }
}