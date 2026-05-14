package cl.duoc.hostly_propiedades_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.duoc.hostly_propiedades_service.dto.UsuarioResponseDTO;

// Cliente Feign para comunicarse con usuarios-service
@FeignClient(
        name = "hostly-usuarios-service",
        url = "http://localhost:8080/api/v1/usuarios"
)
public interface UsuarioClient {

    // Busca un usuario por ID en usuarios-service
    @GetMapping("/{id}")
    UsuarioResponseDTO obtenerUsuarioPorId(@PathVariable Long id);

}