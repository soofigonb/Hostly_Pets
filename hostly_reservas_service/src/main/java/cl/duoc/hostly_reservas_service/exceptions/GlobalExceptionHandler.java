package cl.duoc.hostly_reservas_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    // 1. Manejo de Errores de Validación 
    // Captura los errores de @NotNull, @Positive, etc., en el DTO 
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

    // 2. Manejo de Recurso No Encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(ResourceNotFoundException ex) {
        return crearRespuesta(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 3. Manejo de Errores de Negocio (Fechas, lógica manual) 
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> manejarRuntime(RuntimeException ex) {
        return crearRespuesta(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 4. Manejo de Errores inesperados 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarCualquierError(Exception ex) {
        return crearRespuesta("Ocurrió un error interno inesperado: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Método auxiliar para formato JSON uniforme 
    private ResponseEntity<Map<String, Object>> crearRespuesta(String mensaje, HttpStatus status) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("codigo", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        return new ResponseEntity<>(respuesta, status);
    }
}