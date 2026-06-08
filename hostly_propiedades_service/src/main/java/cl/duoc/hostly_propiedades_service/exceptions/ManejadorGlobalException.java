package cl.duoc.hostly_propiedades_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// Maneja las excepciones globales del microservicio retornando JSON estructurados uniformes
@RestControllerAdvice
public class ManejadorGlobalException {

    // 1. Manejo de Errores de Validación (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> detallesErrores = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            detallesErrores.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("codigo", HttpStatus.BAD_REQUEST.value());
        respuesta.put("error", "Error de Validación");
        respuesta.put("detalles", detallesErrores);

        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    // 2. Manejo de errores de negocio o argumentos incorrectos
    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<Map<String, Object>> manejarErroresNegocio(RuntimeException ex) {
        return crearRespuesta(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 3. Captura cualquier otra excepción no controlada en la aplicación (Error 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcion(Exception ex) {
        return crearRespuesta("Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método auxiliar para construir respuestas en JSON
    private ResponseEntity<Map<String, Object>> crearRespuesta(String mensaje, HttpStatus status) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("codigo", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        return new ResponseEntity<>(respuesta, status);
    }
}
