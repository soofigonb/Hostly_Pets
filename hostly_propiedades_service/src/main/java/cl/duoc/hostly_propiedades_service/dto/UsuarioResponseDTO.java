package cl.duoc.hostly_propiedades_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

// DTO utilizado para recibir información desde usuarios-service mediante FeignClient
@Data
public class UsuarioResponseDTO {

    @NotNull(message = "El id del usuario es obligatorio.")
    private Long idUsuario;

    @NotBlank(message = "El nombre del usuario es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apellido del usuario es obligatorio.")
    private String apellido;

    @Email(message = "El correo del usuario debe tener un formato válido.")
    @NotBlank(message = "El correo del usuario es obligatorio.")
    private String correo;

    @NotBlank(message = "El teléfono del usuario es obligatorio.")
    private String telefono;

    @NotBlank(message = "El rol del usuario es obligatorio.")
    private String rol;

    @NotBlank(message = "El estado del usuario es obligatorio.")
    private String estado;

}
