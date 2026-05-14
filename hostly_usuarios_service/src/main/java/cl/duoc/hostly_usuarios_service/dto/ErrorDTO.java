package cl.duoc.hostly_usuarios_service.dto;

//Importamos LocalDateTime: esta se utiliza para manejar fechas y horas (en nuestro caso, para registrar cuándo ocurre el error)
import java.time.LocalDateTime;

//DTO usado para enviar información de errores al cliente
public class ErrorDTO {

    //Código HTTP del error
    private int status;

    //Mensaje descriptivo del error
    private String mensaje;

    //Fecha y hora en que ocurrió el error
    private LocalDateTime timestamp;

    //Constructor de la clase: este recibe el código de estado y el mensaje del error
    public ErrorDTO(int status, String mensaje){

        //Asigna el código de estado recibido al atributo status
        this.status = status;

        //Asigna el mensaje recibido al atributo mensaje
        this.mensaje = mensaje;

        //Asigna automáticamente la fecha y la hora actual al timestamp, permitiendo saber exactamente cuándo ocurrió el error
        this.timestamp = LocalDateTime.now();
    }

    //Métodos getters y setters para: status, mensaje y timestamp
    public int getStatus(){
        return status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String getMensaje(){
        return mensaje;
    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
}