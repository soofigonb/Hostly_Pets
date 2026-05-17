package cl.duoc.hostly_reservas_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Microservicio propiedades puerto 8081
@FeignClient(name = "hostly-propiedades-service", url = "http://localhost:8081/api/propiedades")
public interface PropiedadClient {

    @GetMapping("/{id}")
    Object obtenerPropiedadPorId(@PathVariable("id") Long id);
}