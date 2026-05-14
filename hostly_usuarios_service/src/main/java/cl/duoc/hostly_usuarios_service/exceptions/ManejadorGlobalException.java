package cl.duoc.hostly_usuarios_service.exceptions;

//Logger: permite registrar mensajes y errores en la aplicación
import org.slf4j.Logger;

//LoggerFactory: crea instancias de Logger
import org.slf4j.LoggerFactory;

//Importamos HttpStatus: este nos sirve para usar códigos HTTP como 404, 409 o 500
import org.springframework.http.HttpStatus;

//Importamos ResponseEntity: este nos permitirá devolver una respuesta HTTP con cuerpo y estado
import org.springframework.http.ResponseEntity;

//Importamos MethodArgumentNotValidException: maneja errores de validación
import org.springframework.web.bind.MethodArgumentNotValidException;

//Importamos ExceptionHandler: nos servirá para indicar qué método manejará una excepción específica
import org.springframework.web.bind.annotation.ExceptionHandler;

//Importamos RestControllerAdvice: este nos permitirá manejar errores de forma global en todos los controladores
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Importamos el DTO de error, ya que este objeto se urará para devolver errores con formato ordenado
import cl.duoc.hostly_usuarios_service.dto.ErrorDTO;


//Maneja las excepciones de forma global
@RestControllerAdvice
public class ManejadorGlobalException {

    //Logger para registrar errores.
    private static final Logger logger = LoggerFactory.getLogger(ManejadorGlobalException.class);

    //Maneja errores cuando un recurso no existe.
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorDTO> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {

        //Registra el error como advertencia.
        logger.warn("Recurso no encontrado: {}", ex.getMessage());

        //Crea la respuesta con código 404.
        ErrorDTO error = new ErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());

        //Retorna el error al cliente.
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Maneja errores cuando un recurso ya existe.
    @ExceptionHandler(RecursoYaExisteException.class)
    public ResponseEntity<ErrorDTO> manejarRecursoYaExiste(RecursoYaExisteException ex) {

        //Registra el conflicto como advertencia.
        logger.warn("Recurso ya existe: {}", ex.getMessage());

        //Crea la respuesta con código 409.
        ErrorDTO error = new ErrorDTO(HttpStatus.CONFLICT.value(), ex.getMessage());

        //Retorna el error al cliente.
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    //Maneja errores de validación del DTO.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> manejarValidaciones(MethodArgumentNotValidException ex) {

        //Obtiene el primer mensaje de validación.
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        //Registra el error de validación.
        logger.warn("Error de validación: {}", mensaje);

        //Crea la respuesta con código 400.
        ErrorDTO error = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), mensaje);

        //Retorna el error al cliente.
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Maneja cualquier error no controlado.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> manejarErrorGeneral(Exception ex) {

        //Registra el error interno.
        logger.error("Error interno del servidor", ex);

        //Crea la respuesta con código 500.
        ErrorDTO error = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor"
        );

        //Retorna el error al cliente.
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
