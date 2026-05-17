package cl.duoc.hostly_reservas_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Apunta al microservicio de usuarios puerto 8080
@FeignClient(name = "hostly-usuarios-service", url = "http://localhost:8080/api/usuarios")
public interface UsuarioClient {

    @GetMapping("/{id}")
    Object obtenerUsuarioPorId(@PathVariable("id") Long id);
}