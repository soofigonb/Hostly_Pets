package cl.duoc.hostly_reservas_service.client;

import cl.duoc.hostly_reservas_service.dto.UsuarioDTO; 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hostly-usuarios-service", url = "http://localhost:8080/api/v1/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id); 
}
