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

import cl.duoc.hostly_propiedades_service.model.TipoMascota;
import cl.duoc.hostly_propiedades_service.repository.TipoMascotaRepository;

@ExtendWith(MockitoExtension.class)
public class TipoMascotaServiceTest {

    @Mock
    private TipoMascotaRepository tipoMascotaRepository;

    @InjectMocks
    private TipoMascotaService tipoMascotaService;

    @Test
    void givenIdTipoMascota_whenObtenerTipoMascotaPorId_thenReturnTipoMascota() {
        // GIVEN
        Long id = 1L;
        TipoMascota tipoMascota = new TipoMascota();
        tipoMascota.setIdTipoMascota(id);
        tipoMascota.setNombreTipoMascota("Perro");

        // WHEN
        when(tipoMascotaRepository.findById(id)).thenReturn(Optional.of(tipoMascota));

        TipoMascota resultado = tipoMascotaService.obtenerTipoMascotaPorId(id);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdTipoMascota());
        assertEquals("Perro", resultado.getNombreTipoMascota());
        verify(tipoMascotaRepository, atMostOnce()).findById(id);
    }

    @Test
    void givenNonExistingId_whenObtenerTipoMascotaPorId_thenThrowIllegalArgumentException() {
        // GIVEN
        Long id = 99L;

        // WHEN
        when(tipoMascotaRepository.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(IllegalArgumentException.class,
                () -> tipoMascotaService.obtenerTipoMascotaPorId(id));
        verify(tipoMascotaRepository, atMostOnce()).findById(id);
    }
}
