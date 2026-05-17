package cl.duoc.hostly_pagos_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos", schema = "db_pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(nullable = false)
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago")
    private MetodoPago metodoPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @ManyToOne
    @JoinColumn(name = "id_estado_pago")
    private EstadoPago estado;
}
