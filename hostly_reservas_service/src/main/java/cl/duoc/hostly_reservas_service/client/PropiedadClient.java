package cl.duoc.hostly_reservas_service.client;

import cl.duoc.hostly_reservas_service.dto.PropiedadDTO; 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hostly-propiedades-service", url = "http://localhost:8081/api/v1/propiedades")
public interface PropiedadClient {

    @GetMapping("/{id}")
    PropiedadDTO obtenerPropiedadPorId(@PathVariable("id") Long id); 
}