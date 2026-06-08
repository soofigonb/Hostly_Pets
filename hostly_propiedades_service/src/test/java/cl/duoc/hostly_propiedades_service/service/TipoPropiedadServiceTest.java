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

import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;
import cl.duoc.hostly_propiedades_service.repository.TipoPropiedadRepository;

@ExtendWith(MockitoExtension.class)
public class TipoPropiedadServiceTest {

    @Mock
    private TipoPropiedadRepository tipoPropiedadRepository;

    @InjectMocks
    private TipoPropiedadService tipoPropiedadService;

    @Test
    void givenIdTipoPropiedad_whenObtenerTipoPropiedadPorId_thenReturnTipoPropiedad() {
        // GIVEN
        Long id = 1L;
        TipoPropiedad tipoPropiedad = new TipoPropiedad();
        tipoPropiedad.setIdTipoPropiedad(id);
        tipoPropiedad.setNombreTipoPropiedad("Casa");

        // WHEN
        when(tipoPropiedadRepository.findById(id)).thenReturn(Optional.of(tipoPropiedad));

        TipoPropiedad resultado = tipoPropiedadService.obtenerTipoPropiedadPorId(id);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getIdTipoPropiedad());
        assertEquals("Casa", resultado.getNombreTipoPropiedad());
        verify(tipoPropiedadRepository, atMostOnce()).findById(id);
    }

    @Test
    void givenNonExistingId_whenObtenerTipoPropiedadPorId_thenThrowIllegalArgumentException() {
        // GIVEN
        Long id = 99L;

        // WHEN
        when(tipoPropiedadRepository.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(IllegalArgumentException.class,
                () -> tipoPropiedadService.obtenerTipoPropiedadPorId(id));
        verify(tipoPropiedadRepository, atMostOnce()).findById(id);
    }
}
