package cl.duoc.hostly_propiedades_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HostlyPropiedadesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostlyPropiedadesServiceApplication.class, args);
	}

}
