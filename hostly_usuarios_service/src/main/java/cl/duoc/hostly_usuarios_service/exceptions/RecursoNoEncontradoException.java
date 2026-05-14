package cl.duoc.hostly_usuarios_service.exceptions;

//Excepción para recursos no encontrados
public class RecursoNoEncontradoException extends RuntimeException {

    //Recibe el mensaje del error
    public RecursoNoEncontradoException (String mensaje){
        super(mensaje);
    }
}