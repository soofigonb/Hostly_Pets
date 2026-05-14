package cl.duoc.hostly_usuarios_service.controller;

//Logger: permite registrar mensajes y eventos de la aplicación
import org.slf4j.Logger;

//LoggerFactory: crea instancias de Logger
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.hostly_usuarios_service.dto.UsuarioDTO;
import cl.duoc.hostly_usuarios_service.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

//Define esta clase como controlador REST
@RestController

//Ruta base para los endpoints de usuarios
@RequestMapping("/api/v1/usuarios")

//Genera el constructor con dependencias final
@RequiredArgsConstructor
public class UsuarioController {

    //Servicio con la lógica de usuarios
    private final UsuarioService usuarioService;

    //Logger para registrar eventos del controlador
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    //Endpoint GET para listar usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios()  {

        //Registra la petición recibida
        logger.info("GET /api/v1/usuarios");

        //Retorna la lista de usuarios
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    //Endpoint GET para buscar usuario por ID
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long idUsuario) {

        //Registra la petición recibida
        logger.info("GET /api/v1/usuarios/{}", idUsuario);

        //Retorna el usuario encontrado
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(idUsuario));
    }

    //Endpoint GET para buscar usuario por email
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorEmail(@PathVariable String email) {

        //Registra la petición recibida
        logger.info("GET /api/v1/usuarios/email/{}", email);

        //Retorna el usuario encontrado
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorEmail(email));
    }

    //Endpoint POST para crear usuario
    @PostMapping
    public ResponseEntity<UsuarioDTO> agregarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {

        //Registra la petición recibida
        logger.info("POST /api/v1/usuarios - Registrando usuario");

        //Crea el usuario usando el servicio
        UsuarioDTO usuarioCreado = usuarioService.agregarUsuario(usuarioDTO);

        //Devuelve HTTP 201 Created con el usuario creado
        return ResponseEntity.status(201).body(usuarioCreado);
    }

    //Endpoint PUT para actualizar usuario
    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long idUsuario, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        
        //Registra la petición recibida
        logger.info("PUT /api/v1/usuarios/{} - Actualizando usuario", idUsuario);

        //Actualiza el usuario usando el servicio
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(idUsuario, usuarioDTO);

        //Devuelve HTTP 200 OK con el usuario actualizado
        return ResponseEntity.ok(usuarioActualizado);
    }

    //Endpoint DELETE para eliminar usuario
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long idUsuario){

        //Registra la petición recibida
        logger.info("DELETE /api/v1/usuarios/{}", idUsuario);

        //Elimina el usuario usando el servicio
        usuarioService.eliminarUsuario(idUsuario);

        //Devuelve HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }
}