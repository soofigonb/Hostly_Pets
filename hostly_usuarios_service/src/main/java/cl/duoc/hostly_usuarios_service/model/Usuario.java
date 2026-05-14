package cl.duoc.hostly_usuarios_service.model;


//Importamos anotaciones de JPA
//Estas sirven para decirle a Spring que esta clase será una tabla en la base de datos
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

//Nombre de la tabla en PostgreSQL
@Table(name = "usuario")
public class Usuario {

    //Clave primaria del usuario
    @Id 

    //Genera el ID automáticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    //Columna obligatoria con máximo 50 caracteres
    @Column(nullable = false, length = 50)
    private String nombre;

    //Columna obligatoria con máximo 50 caracteres
    @Column(nullable = false, length = 50)
    private String apellido;

    //Email único y obligatorio
    @Column(nullable = false, unique = true, length = 250)
    private String email;

    //Teléfono obligatorio con máximo 20 caracteres
    @Column(nullable = false, length = 20)
    private String telefono;

    //Contraseña obligatoria
    @Column(nullable = false, length = 255)
    private String password;

    //Relación muchos a uno con Rol
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    //Relación muchos a uno con EstadoUsuario
    @ManyToOne
    @JoinColumn(name = "id_estado_usuario", nullable = false)
    private EstadoUsuario estado;

}