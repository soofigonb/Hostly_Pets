package cl.duoc.hostly_usuarios_service.dto;

import jakarta.validation.constraints.Email;
//NotBlank: se usa para validar que un texto no esté vacío ni en blanco
import jakarta.validation.constraints.NotBlank;

//NotNull: se usa para validar que un dato no sea nulo
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

//DTO usado para transferir datos del usuario
public class UsuarioDTO {

    //ID del usuario
    private Long idUsuario;

    //Valida que el nombre no esté vacío y tenga máximo 50 caracteres
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    //Valida que el apellido no esté vacío y tenga máximo 50 caracteres
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
    private String apellido;

    //Valida que el email tenga formato válido y máximo 250 caracteres
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 250, message = "El email no puede superar los 250 caracteres")
    private String email;

    //Valida que el teléfono no esté vacío y tenga máximo 20 caracteres
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    private String telefono;

    //Valida que la contraseña no esté vacía y tenga máximo 255 caracteres
    @NotBlank(message = "La password es obligatoria")
    @Size(max = 255, message = "La password no puede superar los 255 caracteres")
    private String password;
    
    //ID del rol asociado al usuario
    @NotNull(message = "El rol es obligatorio")
    private Long idRol;

    //ID del estado asociado al usuario
    @NotNull(message = "El estado es obligatorio")
    private Long idEstadoUsuario;


}