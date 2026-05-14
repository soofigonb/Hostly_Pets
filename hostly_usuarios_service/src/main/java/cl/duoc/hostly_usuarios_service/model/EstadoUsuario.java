package cl.duoc.hostly_usuarios_service.model;

//Importamos anotaciones de JPA
//Estas sirven para decirle a Spring que esta clase será una tabla en la base de datos
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//Importamos Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

//Define esta clase como entidad JPA
@Entity

//Definimos el nombre real de la tabla en PostgreSQL
@Table(name = "estado_usuario" )
public class EstadoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_usuario")
    private Long idEstadoUsuario;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 30)
    private String nombreEstado;
}