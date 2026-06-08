package cl.duoc.hostly_propiedades_service.service;

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

import cl.duoc.hostly_propiedades_service.model.TamanoMascota;
import cl.duoc.hostly_propiedades_service.repository.TamanoMascotaRepository;

@ExtendWith(MockitoExtension.class)
public class TamanoMascotaServiceTest {

    @Mock
    private TamanoMascotaRepository tamanoMascotaRepository;

    @InjectMocks
    private TamanoMascotaService tamanoMascotaService;

    @Test
    void givenIdTamanoMascota_whenObtenerTamanoMascotaPorId_thenReturnTamanoMascota() {
        // GIVEN
        Long id = 1L;
        TamanoMascota tamanoMascota = new TamanoMascota();
        tamanoMascota.setIdTamanoMascota(id);
        tamanoMascota.setNombreTamanoMascota("Pequeño");

        // WHEN
        when(tamanoMascotaRepository.findById(id)).thenReturn(Optional.of(tamanoMascota));

        TamanoMascota resultado = tamanoMascotaService.obtenerTamanoMascotaPorId(id);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdTamanoMascota());
        assertEquals("Pequeño", resultado.getNombreTamanoMascota());
        verify(tamanoMascotaRepository, atMostOnce()).findById(id);
    }

    @Test
    void givenNonExistingId_whenObtenerTamanoMascotaPorId_thenThrowIllegalArgumentException() {
        // GIVEN
        Long id = 99L;

        // WHEN
        when(tamanoMascotaRepository.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(IllegalArgumentException.class,
                () -> tamanoMascotaService.obtenerTamanoMascotaPorId(id));
        verify(tamanoMascotaRepository, atMostOnce()).findById(id);
    }
}
