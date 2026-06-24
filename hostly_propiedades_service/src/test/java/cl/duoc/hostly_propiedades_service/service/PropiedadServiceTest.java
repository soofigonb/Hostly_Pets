package cl.duoc.hostly_propiedades_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.hostly_propiedades_service.clients.UsuarioClient;
import cl.duoc.hostly_propiedades_service.dto.PropiedadRequestDTO;
import cl.duoc.hostly_propiedades_service.dto.UsuarioResponseDTO;
import cl.duoc.hostly_propiedades_service.model.Propiedad;
import cl.duoc.hostly_propiedades_service.model.TamanoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;
import cl.duoc.hostly_propiedades_service.repository.PropiedadRepository;

// Inicializa los mocks de Mockito para las pruebas unitarias
@ExtendWith(MockitoExtension.class)
public class PropiedadServiceTest {

    @Mock
    private PropiedadRepository propiedadRepository;

    @Mock
    private TipoPropiedadService tipoPropiedadService;

    @Mock
    private TipoMascotaService tipoMascotaService;

    @Mock
    private TamanoMascotaService tamanoMascotaService;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks // Servicio real bajo prueba con sus dependencias mockeadas
    private PropiedadService propiedadService;

    private Propiedad buildPropiedad(Long id) {
        TipoPropiedad tipo = new TipoPropiedad();
        tipo.setIdTipoPropiedad(1L);
        tipo.setNombreTipoPropiedad("Casa");

        TipoMascota tipoMascota = new TipoMascota();
        tipoMascota.setIdTipoMascota(1L);
        tipoMascota.setNombreTipoMascota("Perro");

        TamanoMascota tamanoMascota = new TamanoMascota();
        tamanoMascota.setIdTamanoMascota(1L);
        tamanoMascota.setNombreTamanoMascota("Pequeño");

        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(id);
        propiedad.setIdAnfitrion(10L);
        propiedad.setTitulo("Casa en Santiago");
        propiedad.setDescripcion("Amplia casa pet-friendly");
        propiedad.setDireccion("Av. Principal 123");
        propiedad.setCiudad("Santiago");
        propiedad.setPrecioNoche(50000.0);
        propiedad.setTienePatio(true);
        propiedad.setCostoExtraMascota(5000.0);
        propiedad.setDisponible(true);
        propiedad.setTipoPropiedad(tipo);
        propiedad.setTipoMascota(tipoMascota);
        propiedad.setTamanoMascota(tamanoMascota);
        return propiedad;
    }

    private PropiedadRequestDTO buildRequestDTO() {
        PropiedadRequestDTO dto = new PropiedadRequestDTO();
        dto.setIdAnfitrion(10L);
        dto.setTitulo("Casa en Santiago");
        dto.setDescripcion("Amplia casa pet-friendly");
        dto.setDireccion("Av. Principal 123");
        dto.setCiudad("Santiago");
        dto.setPrecioNoche(50000.0);
        dto.setTienePatio(true);
        dto.setCostoExtraMascota(5000.0);
        dto.setDisponible(true);
        dto.setIdTipoPropiedad(1L);
        dto.setIdTipoMascota(1L);
        dto.setIdTamanoMascota(1L);
        return dto;
    }

    private UsuarioResponseDTO buildAnfitrion() {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setIdUsuario(10L);
        usuario.setNombre("Carlos");
        usuario.setApellido("Pérez");
        usuario.setCorreo("carlos@test.com");
        usuario.setTelefono("+56912345678");
        usuario.setRol("3");
        usuario.setEstado("Activo");
        return usuario;
    }

    @Test
    void whenObtenerPropiedades_thenReturnListaPropiedades() {
        Propiedad propiedad = buildPropiedad(1L);

        when(propiedadRepository.findAll()).thenReturn(List.of(propiedad));

        List<Propiedad> resultado = propiedadService.obtenerPropiedades();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(propiedadRepository, atMostOnce()).findAll();
    }

    @Test
    void whenObtenerPropiedadesVacio_thenReturnListaVacia() {
        when(propiedadRepository.findAll()).thenReturn(List.of());

        List<Propiedad> resultado = propiedadService.obtenerPropiedades();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    void givenIdPropiedad_whenObtenerPropiedadPorId_thenReturnPropiedad() {
        Long id = 1L;
        Propiedad propiedad = buildPropiedad(id);

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedad));

        Propiedad resultado = propiedadService.obtenerPropiedadPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdPropiedad());
        assertEquals("Casa en Santiago", resultado.getTitulo());
        verify(propiedadRepository, atMostOnce()).findById(id);
    }

    @Test
    void givenNonExistingId_whenObtenerPropiedadPorId_thenThrowIllegalArgumentException() {
        Long id = 99L;

        when(propiedadRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.obtenerPropiedadPorId(id));

        verify(propiedadRepository, atMostOnce()).findById(id);
    }

    //Crear porpiedad
    @Test
    void givenPropiedadData_whenCrearPropiedad_thenReturnPropiedadGuardada() {
        PropiedadRequestDTO dto = buildRequestDTO();
        UsuarioResponseDTO anfitrion = buildAnfitrion();

        TipoPropiedad tipoPropiedad = new TipoPropiedad(1L, "Casa");
        TipoMascota tipoMascota = new TipoMascota(1L, "Perro");
        TamanoMascota tamanoMascota = new TamanoMascota(1L, "Pequeño");

        Propiedad propiedadGuardada = buildPropiedad(1L);

        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(anfitrion);
        when(tipoPropiedadService.obtenerTipoPropiedadPorId(dto.getIdTipoPropiedad())).thenReturn(tipoPropiedad);
        when(tipoMascotaService.obtenerTipoMascotaPorId(dto.getIdTipoMascota())).thenReturn(tipoMascota);
        when(tamanoMascotaService.obtenerTamanoMascotaPorId(dto.getIdTamanoMascota())).thenReturn(tamanoMascota);
        when(propiedadRepository.save(any(Propiedad.class))).thenReturn(propiedadGuardada);

        Propiedad resultado = propiedadService.crearPropiedad(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdPropiedad());
        assertEquals("Casa en Santiago", resultado.getTitulo());
        verify(usuarioClient, atMostOnce()).obtenerUsuarioPorId(dto.getIdAnfitrion());
        verify(propiedadRepository, atMostOnce()).save(any(Propiedad.class));
    }

    @Test
    void givenAnfitrionConRolIncorrecto_whenCrearPropiedad_thenThrowIllegalArgumentException() {
        PropiedadRequestDTO dto = buildRequestDTO();

        UsuarioResponseDTO usuarioSinRol = buildAnfitrion();
        usuarioSinRol.setRol("Huésped");

        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(usuarioSinRol);

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.crearPropiedad(dto));
    }

    @Test
    void givenAnfitrionNulo_whenCrearPropiedad_thenThrowIllegalArgumentException() {
        PropiedadRequestDTO dto = buildRequestDTO();

        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.crearPropiedad(dto));
    }

    @Test
    void givenRolNulo_whenCrearPropiedad_thenThrowIllegalArgumentException() {
        PropiedadRequestDTO dto = buildRequestDTO();

        UsuarioResponseDTO usuario = buildAnfitrion();
        usuario.setRol(null);

        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(usuario);

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.crearPropiedad(dto));
    }

    @Test
    void givenPropiedadData_whenActualizarPropiedad_thenReturnPropiedadActualizada() {
        Long id = 1L;
        PropiedadRequestDTO dto = buildRequestDTO();
        dto.setTitulo("Casa Modificada");

        UsuarioResponseDTO anfitrion = buildAnfitrion();
        Propiedad propiedadExistente = buildPropiedad(id);
        Propiedad propiedadActualizada = buildPropiedad(id);
        propiedadActualizada.setTitulo("Casa Modificada");

        TipoPropiedad tipoPropiedad = new TipoPropiedad(1L, "Casa");
        TipoMascota tipoMascota = new TipoMascota(1L, "Perro");
        TamanoMascota tamanoMascota = new TamanoMascota(1L, "Pequeño");

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedadExistente));
        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(anfitrion);
        when(tipoPropiedadService.obtenerTipoPropiedadPorId(dto.getIdTipoPropiedad())).thenReturn(tipoPropiedad);
        when(tipoMascotaService.obtenerTipoMascotaPorId(dto.getIdTipoMascota())).thenReturn(tipoMascota);
        when(tamanoMascotaService.obtenerTamanoMascotaPorId(dto.getIdTamanoMascota())).thenReturn(tamanoMascota);
        when(propiedadRepository.save(any(Propiedad.class))).thenReturn(propiedadActualizada);

        Propiedad resultado = propiedadService.actualizarPropiedad(id, dto);

        assertNotNull(resultado);
        assertEquals("Casa Modificada", resultado.getTitulo());
        verify(propiedadRepository, atMostOnce()).findById(id);
        verify(propiedadRepository, atMostOnce()).save(any(Propiedad.class));
    }

    @Test
    void givenNonExistingId_whenActualizarPropiedad_thenThrowIllegalArgumentException() {
        Long id = 99L;
        PropiedadRequestDTO dto = buildRequestDTO();

        when(propiedadRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.actualizarPropiedad(id, dto));
    }

    @Test
    void givenAnfitrionConRolIncorrecto_whenActualizarPropiedad_thenThrowIllegalArgumentException() {
        Long id = 1L;
        PropiedadRequestDTO dto = buildRequestDTO();
        Propiedad propiedadExistente = buildPropiedad(id);

        UsuarioResponseDTO usuarioSinRol = buildAnfitrion();
        usuarioSinRol.setRol("Huésped");

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedadExistente));
        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(usuarioSinRol);

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.actualizarPropiedad(id, dto));
    }

    @Test
    void givenAnfitrionNulo_whenActualizarPropiedad_thenThrowIllegalArgumentException() {
        Long id = 1L;
        PropiedadRequestDTO dto = buildRequestDTO();
        Propiedad propiedadExistente = buildPropiedad(id);

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedadExistente));
        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.actualizarPropiedad(id, dto));
    }

    @Test
    void givenRolNulo_whenActualizarPropiedad_thenThrowIllegalArgumentException() {
        Long id = 1L;
        PropiedadRequestDTO dto = buildRequestDTO();
        Propiedad propiedadExistente = buildPropiedad(id);

        UsuarioResponseDTO usuario = buildAnfitrion();
        usuario.setRol(null);

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedadExistente));
        when(usuarioClient.obtenerUsuarioPorId(dto.getIdAnfitrion())).thenReturn(usuario);

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.actualizarPropiedad(id, dto));
    }

    @Test
    void givenIdPropiedad_whenEliminarPropiedad_thenPropiedadQuedaInhabilitada() {
        Long id = 1L;
        Propiedad propiedad = buildPropiedad(id);

        when(propiedadRepository.findById(id)).thenReturn(Optional.of(propiedad));
        when(propiedadRepository.save(any(Propiedad.class))).thenReturn(propiedad);

        propiedadService.eliminarPropiedad(id);

        assertFalse(propiedad.getDisponible());
        verify(propiedadRepository, atMostOnce()).findById(id);
        verify(propiedadRepository, atMostOnce()).save(any(Propiedad.class));
    }

    @Test
    void givenNonExistingId_whenEliminarPropiedad_thenThrowIllegalArgumentException() {
        Long id = 99L;

        when(propiedadRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> propiedadService.eliminarPropiedad(id));
    }

    @Test
    void givenCiudad_whenObtenerPropiedadesPorCiudad_thenReturnLista() {
        String ciudad = "Santiago";
        Propiedad propiedad = buildPropiedad(1L);

        when(propiedadRepository.findByCiudad(ciudad)).thenReturn(List.of(propiedad));

        List<Propiedad> resultado = propiedadService.obtenerPropiedadesPorCiudad(ciudad);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(ciudad, resultado.get(0).getCiudad());
        verify(propiedadRepository, atMostOnce()).findByCiudad(ciudad);
    }

    @Test
    void whenObtenerPropiedadesDisponibles_thenReturnListaDisponibles() {
        Propiedad propiedad = buildPropiedad(1L);

        when(propiedadRepository.findByDisponible(true)).thenReturn(List.of(propiedad));

        List<Propiedad> resultado = propiedadService.obtenerPropiedadesDisponibles();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(propiedadRepository, atMostOnce()).findByDisponible(true);
    }

    @Test
    void givenIdAnfitrion_whenObtenerPropiedadesPorAnfitrion_thenReturnLista() {
        Long idAnfitrion = 10L;
        Propiedad propiedad = buildPropiedad(1L);

        when(propiedadRepository.findByIdAnfitrion(idAnfitrion)).thenReturn(List.of(propiedad));

        List<Propiedad> resultado = propiedadService.obtenerPropiedadesPorAnfitrion(idAnfitrion);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(idAnfitrion, resultado.get(0).getIdAnfitrion());
        verify(propiedadRepository, atMostOnce()).findByIdAnfitrion(idAnfitrion);
    }
}