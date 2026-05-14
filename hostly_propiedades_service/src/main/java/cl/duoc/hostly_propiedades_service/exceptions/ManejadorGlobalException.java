package cl.duoc.hostly_propiedades_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Maneja las excepciones globales del microservicio
@RestControllerAdvice
public class ManejadorGlobalException {

    // Captura cualquier excepción no controlada en la aplicación
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepcion(Exception ex) {

        // Retorna error 500 con el mensaje de la excepción
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + ex.getMessage());

    }

}
