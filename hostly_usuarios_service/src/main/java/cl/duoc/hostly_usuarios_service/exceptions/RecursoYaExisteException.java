package cl.duoc.hostly_usuarios_service.exceptions;

//Excepción para recursos que ya existen
public class RecursoYaExisteException extends RuntimeException{

    //Recibe el mensaje del error
    public RecursoYaExisteException(String mensaje){
        super(mensaje);
    }
}