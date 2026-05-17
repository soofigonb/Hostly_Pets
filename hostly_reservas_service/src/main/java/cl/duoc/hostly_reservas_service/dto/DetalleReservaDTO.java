package cl.duoc.hostly_reservas_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetalleReservaDTO {
    private Integer id;
    private BigDecimal montoBase;
    private BigDecimal montoMascota;
    private BigDecimal total;
}
