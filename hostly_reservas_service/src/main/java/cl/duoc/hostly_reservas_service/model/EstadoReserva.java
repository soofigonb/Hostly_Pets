package cl.duoc.hostly_reservas_service.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estados_reserva", schema = "db_reservas")
public class EstadoReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    public EstadoReserva() {}

    public EstadoReserva(Long id) {
        this.id = id;
    }
}