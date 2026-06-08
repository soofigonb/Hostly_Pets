package cl.duoc.hostly_usuarios_service.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.hostly_usuarios_service.dto.UsuarioDTO;
import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;
import cl.duoc.hostly_usuarios_service.service.UsuarioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

// Carga solo la capa web (Controlador) para pruebas más rápidas y aisladas
@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Utilizado para simular las peticiones HTTP REST

    @MockitoBean // Crea un mock del servicio y lo inyecta en el controlador
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void whenObtenerTodosLosUsuarios_thenReturnOk() throws Exception {

        // GIVEN
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1L);

        List<UsuarioDTO> usuarios = List.of(usuarioDTO);

        // WHEN
        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(usuarios);

        // THEN
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idUsuario").value(1L));
    }

    @Test
    void whenObtenerUsuarioPorId_thenReturnOk() throws Exception {

        // GIVEN
        Long idUsuario = 1L;

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(idUsuario);
        usuarioDTO.setEmail("test@test.com");

        // WHEN
        when(usuarioService.obtenerUsuarioPorId(idUsuario)).thenReturn(usuarioDTO);

        // THEN
        mockMvc.perform(get("/api/v1/usuarios/{idUsuario}", idUsuario))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(idUsuario))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void whenObtenerUsuarioPorId_thenReturnNotFound() throws Exception {

        // GIVEN
        Long idUsuario = 99L;

        // WHEN
        when(usuarioService.obtenerUsuarioPorId(idUsuario))
                .thenThrow(new RecursoNoEncontradoException("Usuario no encontrado"));

        // THEN
        mockMvc.perform(get("/api/v1/usuarios/{idUsuario}", idUsuario))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenObtenerUsuarioPorEmail_thenReturnOk() throws Exception {

        // GIVEN
        String email = "juan@test.com";

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1L);
        usuarioDTO.setEmail(email);

        // WHEN
        when(usuarioService.obtenerUsuarioPorEmail(email)).thenReturn(usuarioDTO);

        // THEN
        mockMvc.perform(get("/api/v1/usuarios/email/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void whenAgregarUsuario_thenReturnCreated() throws Exception {

        // GIVEN
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setNombre("Juan");
        nuevoUsuario.setApellido("Perez");
        nuevoUsuario.setEmail("juan@test.com");
        nuevoUsuario.setTelefono("+56912345678");
        nuevoUsuario.setPassword("Pass1234");
        nuevoUsuario.setIdRol(1L);
        nuevoUsuario.setIdEstadoUsuario(1L);

        UsuarioDTO usuarioCreado = new UsuarioDTO();
        usuarioCreado.setIdUsuario(1L);
        usuarioCreado.setNombre("Juan");
        usuarioCreado.setApellido("Perez");
        usuarioCreado.setEmail("juan@test.com");

        // WHEN
        when(usuarioService.agregarUsuario(any(UsuarioDTO.class))).thenReturn(usuarioCreado);

        // THEN
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void whenAgregarUsuarioConDatosInvalidos_thenReturnBadRequest() throws Exception {

        // GIVEN
        UsuarioDTO usuarioInvalido = new UsuarioDTO();
        usuarioInvalido.setNombre("");
        usuarioInvalido.setApellido("");
        usuarioInvalido.setEmail("correo-malo");
        usuarioInvalido.setTelefono("abc");
        usuarioInvalido.setPassword("123");
        usuarioInvalido.setIdRol(null);
        usuarioInvalido.setIdEstadoUsuario(null);

        // WHEN AND THEN
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenActualizarUsuario_thenReturnOk() throws Exception {

        // GIVEN
        Long idUsuario = 1L;

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan Modificado");
        usuarioDTO.setApellido("Perez");
        usuarioDTO.setEmail("juan@test.com");
        usuarioDTO.setTelefono("+56912345678");
        usuarioDTO.setPassword("Pass1234");
        usuarioDTO.setIdRol(1L);
        usuarioDTO.setIdEstadoUsuario(1L);

        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setIdUsuario(idUsuario);
        usuarioActualizado.setNombre("Juan Modificado");

        // WHEN
        when(usuarioService.actualizarUsuario(eq(idUsuario), any(UsuarioDTO.class)))
                .thenReturn(usuarioActualizado);

        // THEN
        mockMvc.perform(put("/api/v1/usuarios/{idUsuario}", idUsuario)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Modificado"));
    }

    @Test
    void whenEliminarUsuario_thenReturnNoContent() throws Exception {

        // GIVEN
        Long idUsuario = 1L;

        // WHEN
        when(usuarioService.eliminarUsuario(idUsuario)).thenReturn(true);

        // THEN
        mockMvc.perform(delete("/api/v1/usuarios/{idUsuario}", idUsuario))
                .andExpect(status().isNoContent());
    }
}