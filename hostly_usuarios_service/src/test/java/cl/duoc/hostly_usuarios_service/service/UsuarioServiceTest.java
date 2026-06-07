package cl.duoc.hostly_usuarios_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.hostly_usuarios_service.dto.UsuarioDTO;
import cl.duoc.hostly_usuarios_service.dto.UsuarioDTOMapper;
import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;
import cl.duoc.hostly_usuarios_service.exceptions.RecursoYaExisteException;
import cl.duoc.hostly_usuarios_service.model.Usuario;
import cl.duoc.hostly_usuarios_service.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    // Mocks
    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private UsuarioDTOMapper usuarioDTOMapper;

    // Servicio por probar
    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void givenIdUsuario_whenObtenerUsuarioPorId_thenReturnUsuarioDTO() {

        // GIVEN
        Long idUsuario = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setEmail("test@test.com");

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(idUsuario);
        usuarioDTO.setEmail("test@test.com");

        // WHEN
        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioDTOMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.obtenerUsuarioPorId(idUsuario);

        // THEN
        assertNotNull(resultado);
        assertEquals(idUsuario, resultado.getIdUsuario());
        assertEquals("test@test.com", resultado.getEmail());
        verify(usuarioRepo, atMostOnce()).findById(idUsuario);
    }

    @Test
    void givenNonExistingIdUsuario_whenObtenerUsuarioPorId_thenThrowRecursoNoEncontradoException() {

        // GIVEN
        Long idUsuario = 99L;

        // WHEN
        when(usuarioRepo.findById(idUsuario)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.obtenerUsuarioPorId(idUsuario));
        verify(usuarioRepo, atMostOnce()).findById(idUsuario);
    }

    @Test
    void givenUsuarioData_whenAgregarUsuario_thenReturnUsuarioDTO() {

        // GIVEN
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("nuevo@test.com");

        Usuario usuario = new Usuario();
        usuario.setEmail("nuevo@test.com");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setIdUsuario(1L);
        usuarioGuardado.setEmail("nuevo@test.com");

        UsuarioDTO usuarioDTOCreado = new UsuarioDTO();
        usuarioDTOCreado.setIdUsuario(1L);
        usuarioDTOCreado.setEmail("nuevo@test.com");

        // WHEN
        when(usuarioRepo.existsByEmail(usuarioDTO.getEmail())).thenReturn(false);
        when(usuarioDTOMapper.toModel(usuarioDTO)).thenReturn(usuario);
        when(usuarioRepo.save(usuario)).thenReturn(usuarioGuardado);
        when(usuarioDTOMapper.toDTO(usuarioGuardado)).thenReturn(usuarioDTOCreado);

        UsuarioDTO resultado = usuarioService.agregarUsuario(usuarioDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        verify(usuarioRepo, atMostOnce()).existsByEmail(usuarioDTO.getEmail());
        verify(usuarioRepo, atMostOnce()).save(usuario);
    }

    @Test
    void givenExistingEmail_whenAgregarUsuario_thenThrowRecursoYaExisteException() {

        // GIVEN
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("existente@test.com");

        // WHEN
        when(usuarioRepo.existsByEmail(usuarioDTO.getEmail())).thenReturn(true);

        // THEN
        assertThrows(RecursoYaExisteException.class, () -> usuarioService.agregarUsuario(usuarioDTO));
        verify(usuarioRepo, atMostOnce()).existsByEmail(usuarioDTO.getEmail());
    }

    @Test
    void givenIdUsuario_whenEliminarUsuario_thenReturnTrue() {

        // GIVEN
        Long idUsuario = 1L;

        // WHEN
        when(usuarioRepo.existsById(idUsuario)).thenReturn(true);

        boolean resultado = usuarioService.eliminarUsuario(idUsuario);

        // THEN
        assertTrue(resultado);
        verify(usuarioRepo, atMostOnce()).existsById(idUsuario);
        verify(usuarioRepo, atMostOnce()).deleteById(idUsuario);
    }

    @Test
    void whenObtenerTodosLosUsuarios_thenReturnListaUsuarios() {
        // GIVEN
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        List<Usuario> usuarios = List.of(usuario);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(1L);

        // WHEN
        when(usuarioRepo.findAll()).thenReturn(usuarios);
        when(usuarioDTOMapper.toDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        List<UsuarioDTO> resultado = usuarioService.obtenerTodosLosUsuarios();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepo, atMostOnce()).findAll();
    }

    @Test
    void whenObtenerTodosLosUsuariosVacio_thenThrowRecursoNoEncontradoException() {
        // WHEN
        when(usuarioRepo.findAll()).thenReturn(List.of());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.obtenerTodosLosUsuarios());
    }

    @Test
    void givenEmail_whenObtenerUsuarioPorEmail_thenReturnUsuarioDTO() {
        // GIVEN
        String email = "test@test.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail(email);

        // WHEN
        when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioDTOMapper.toDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.obtenerUsuarioPorEmail(email);

        // THEN
        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
    }

    @Test
    void givenNonExistingEmail_whenObtenerUsuarioPorEmail_thenThrowRecursoNoEncontradoException() {
        // GIVEN
        String email = "no@test.com";

        // WHEN
        when(usuarioRepo.findByEmail(email)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.obtenerUsuarioPorEmail(email));
    }

    @Test
    void givenUsuarioData_whenActualizarUsuario_thenReturnUsuarioDTO() {
        // GIVEN
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("modificado@test.com");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setIdUsuario(id);
        usuarioExistente.setEmail("original@test.com");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setIdUsuario(id);
        usuarioActualizado.setEmail("modificado@test.com");

        // WHEN
        when(usuarioRepo.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepo.existsByEmail("modificado@test.com")).thenReturn(false);
        when(usuarioDTOMapper.toModel(usuarioDTO)).thenReturn(usuarioActualizado);
        when(usuarioRepo.save(usuarioActualizado)).thenReturn(usuarioActualizado);
        when(usuarioDTOMapper.toDTO(usuarioActualizado)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.actualizarUsuario(id, usuarioDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("modificado@test.com", resultado.getEmail());
    }

    @Test
    void givenExistingEmail_whenActualizarUsuario_thenThrowRecursoYaExisteException() {
        // GIVEN
        Long id = 1L;
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("otro@test.com");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setIdUsuario(id);
        usuarioExistente.setEmail("original@test.com");

        // WHEN
        when(usuarioRepo.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepo.existsByEmail("otro@test.com")).thenReturn(true);

        // THEN
        assertThrows(RecursoYaExisteException.class, () -> usuarioService.actualizarUsuario(id, usuarioDTO));
    }

    @Test
    void givenNonExistingIdUsuario_whenEliminarUsuario_thenThrowRecursoNoEncontradoException() {

        // GIVEN
        Long idUsuario = 99L;

        // WHEN
        when(usuarioRepo.existsById(idUsuario)).thenReturn(false);

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> usuarioService.eliminarUsuario(idUsuario));
        verify(usuarioRepo, atMostOnce()).existsById(idUsuario);
    }
}
