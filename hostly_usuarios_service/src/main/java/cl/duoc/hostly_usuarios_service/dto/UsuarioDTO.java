package cl.duoc.hostly_usuarios_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Datos de un usuario del sistema")
@Data
public class UsuarioDTO {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long idUsuario;

    @Schema(description = "Nombre del usuario", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede superar los 50 caracteres")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 250, message = "El email no puede superar los 250 caracteres")
    private String email;

    @Schema(description = "Teléfono de contacto del usuario (formato internacional)", example = "+56912345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "El teléfono debe contener solo números y opcionalmente un prefijo '+'")
    private String telefono;

    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres, con mayúscula, minúscula y número)", example = "Password1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La password es obligatoria")
    @Size(max = 255, message = "La password no puede superar los 255 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
    private String password;

    @Schema(description = "ID del rol asignado al usuario (1=Admin, 2=Anfitrión, 3=Huésped)", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El rol es obligatorio")
    private Long idRol;

    @Schema(description = "ID del estado del usuario (1=Activo, 2=Inactivo)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El estado es obligatorio")
    private Long idEstadoUsuario;
}