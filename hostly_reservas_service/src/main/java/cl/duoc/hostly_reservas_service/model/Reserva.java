package cl.duoc.hostly_reservas_service.model;

import jakarta.persistence.*; 
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "reservas", schema = "db_reservas")
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario") 
    private Long idUsuario;

    @Column(name = "id_propiedad") 
    private Long idPropiedad;

    private LocalDate fechaInicio; 
    private LocalDate fechaFin;    

    private Integer cantidadMascotas; 
    private String tipoMascota;       
    private String tamanoMascota;     

    private Double totalReserva; 

    //RELACIÓN CLAVE:
    @ManyToOne
    @JoinColumn(name = "id_estado_reserva") 
    private EstadoReserva estado;

}