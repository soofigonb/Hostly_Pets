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
import cl.duoc.hostly_usuarios_service.model.Rol;
import cl.duoc.hostly_usuarios_service.repository.RolRepository;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @Mock
    private RolRepository rolRepo;

    @InjectMocks
    private RolService rolService;

    @Test
    void givenIdRol_whenFindById_thenReturnRol() {
        // GIVEN
        Long idRol = 1L;
        Rol rol = new Rol();
        rol.setIdRol(idRol);
        rol.setNombreRol("ADMIN");

        // WHEN
        when(rolRepo.findById(idRol)).thenReturn(Optional.of(rol));

        Rol resultado = rolService.findById(idRol);

        // THEN
        assertNotNull(resultado);
        assertEquals(idRol, resultado.getIdRol());
        assertEquals("ADMIN", resultado.getNombreRol());
        verify(rolRepo, atMostOnce()).findById(idRol);
    }

    @Test
    void givenNullId_whenFindById_thenThrowIllegalArgumentException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> rolService.findById(null));
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowRecursoNoEncontradoException() {
        // GIVEN
        Long idRol = 99L;

        // WHEN
        when(rolRepo.findById(idRol)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> rolService.findById(idRol));
        verify(rolRepo, atMostOnce()).findById(idRol);
    }

    @Test
    void givenNombreRol_whenFindByNombreRol_thenReturnRol() {
        // GIVEN
        String nombreRol = "ADMIN";
        Rol rol = new Rol();
        rol.setIdRol(1L);
        rol.setNombreRol(nombreRol);

        // WHEN
        when(rolRepo.findByNombreRol(nombreRol)).thenReturn(Optional.of(rol));

        Rol resultado = rolService.findByNombreRol(nombreRol);

        // THEN
        assertNotNull(resultado);
        assertEquals(nombreRol, resultado.getNombreRol());
        verify(rolRepo, atMostOnce()).findByNombreRol(nombreRol);
    }

    @Test
    void givenNullNombre_whenFindByNombreRol_thenThrowIllegalArgumentException() {
        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> rolService.findByNombreRol(null));
        assertThrows(IllegalArgumentException.class, () -> rolService.findByNombreRol(""));
    }

    @Test
    void givenNonExistingNombre_whenFindByNombreRol_thenThrowRecursoNoEncontradoException() {
        // GIVEN
        String nombreRol = "INEXISTENTE";

        // WHEN
        when(rolRepo.findByNombreRol(nombreRol)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RecursoNoEncontradoException.class, () -> rolService.findByNombreRol(nombreRol));
        verify(rolRepo, atMostOnce()).findByNombreRol(nombreRol);
    }
}
