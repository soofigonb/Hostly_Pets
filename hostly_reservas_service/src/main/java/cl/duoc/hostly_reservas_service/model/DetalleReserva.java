package cl.duoc.hostly_reservas_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_reserva", schema = "db_reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", nullable = false)
    @JsonBackReference
    private Reserva reserva;

    @Column(name = "monto_base", nullable = false)
    private BigDecimal montoBase;

    @Column(name = "monto_mascota", nullable = false)
    private BigDecimal montoMascota;

    @Column(name = "total", nullable = false)
    private BigDecimal total;
}
