package cl.duoc.hostly_usuarios_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.hostly_usuarios_service.exceptions.RecursoNoEncontradoException;
import cl.duoc.hostly_usuarios_service.model.EstadoUsuario;
import cl.duoc.hostly_usuarios_service.repository.EstadoUsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class EstadoUsuarioServiceTest {

    @Mock
    private EstadoUsuarioRepository estadoUsuarioRepo;

    @InjectMocks
    private EstadoUsuarioService estadoUsuarioService;

    @Test
    void givenIdEstado_whenFindById_thenReturnEstado() {
        // GIVEN
        Long idEstado = 1L;
        EstadoUsuario estado = new EstadoUsuario();
        estado.setIdEstadoUsuario(idEstado);
        estado.setNombreEstado("ACTIVO");

        // WHEN
        when(estadoUsuarioRepo.findById(idEstado)).thenReturn(Optional.of(estado));

        EstadoUsuario resultado = estadoUsuarioService.findById(idEstado);

        // THEN
        assertNotNull(resultado);
        assertEquals(idEstado, resultado.getIdEstadoUsuario());
        assertEquals("ACTIVO", resultado.getNombreEstado());
        verify(estadoUsuarioRepo, atMostOnce()).findById(idEstado);
    }

    @Test
    void givenNullId_whenFindById_thenThrowIllegalArgumentException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> estadoUsuarioService.findById(null));
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowRecursoNoEncontradoException() {
        // GIVEN
        Long idEstado = 99L;

        // WHEN
        when(estadoUsuarioRepo.findById(idEstado)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> estadoUsuarioService.findById(idEstado));
        verify(estadoUsuarioRepo, atMostOnce()).findById(idEstado);
    }

    @Test
    void givenNombreEstado_whenFindByNombreEstado_thenReturnEstado() {
        // GIVEN
        String nombreEstado = "ACTIVO";
        EstadoUsuario estado = new EstadoUsuario();
        estado.setIdEstadoUsuario(1L);
        estado.setNombreEstado(nombreEstado);

        // WHEN
        when(estadoUsuarioRepo.findByNombreEstado(nombreEstado)).thenReturn(Optional.of(estado));

        EstadoUsuario resultado = estadoUsuarioService.findByNombreEstado(nombreEstado);

        // THEN
        assertNotNull(resultado);
        assertEquals(nombreEstado, resultado.getNombreEstado());
        verify(estadoUsuarioRepo, atMostOnce()).findByNombreEstado(nombreEstado);
    }

    @Test
    void givenNullNombre_whenFindByNombreEstado_thenThrowIllegalArgumentException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> estadoUsuarioService.findByNombreEstado(null));
        assertThrows(IllegalArgumentException.class, () -> estadoUsuarioService.findByNombreEstado(""));
    }

    @Test
    void givenNonExistingNombre_whenFindByNombreEstado_thenThrowRecursoNoEncontradoException() {
        // GIVEN
        String nombreEstado = "INEXISTENTE";

        // WHEN
        when(estadoUsuarioRepo.findByNombreEstado(nombreEstado)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> estadoUsuarioService.findByNombreEstado(nombreEstado));
        verify(estadoUsuarioRepo, atMostOnce()).findByNombreEstado(nombreEstado);
    }
}
