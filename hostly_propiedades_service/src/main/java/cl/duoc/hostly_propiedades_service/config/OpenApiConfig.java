package cl.duoc.hostly_propiedades_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hostly Pets - Propiedades API")
                        .version("1.0")
                        .description("API para la gestión de propiedades de Hostly Pets"));
    }
}
