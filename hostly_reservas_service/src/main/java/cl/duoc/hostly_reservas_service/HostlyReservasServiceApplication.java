package cl.duoc.hostly_reservas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HostlyReservasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostlyReservasServiceApplication.class, args);
	}

}
