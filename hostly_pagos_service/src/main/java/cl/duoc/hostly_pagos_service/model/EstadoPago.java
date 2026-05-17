package cl.duoc.hostly_pagos_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estados_pago", schema = "db_pagos")
public class EstadoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public EstadoPago() {}
    public EstadoPago(Long id) { this.id = id; }
}