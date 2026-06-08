package cl.duoc.hostly_reservas_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.hostly_reservas_service.client.PropiedadClient;
import cl.duoc.hostly_reservas_service.client.UsuarioClient;
import cl.duoc.hostly_reservas_service.dto.PropiedadDTO;
import cl.duoc.hostly_reservas_service.dto.UsuarioDTO;
import cl.duoc.hostly_reservas_service.dto.ReservaDTO;
import cl.duoc.hostly_reservas_service.dto.ReservaMapper;
import cl.duoc.hostly_reservas_service.exceptions.ResourceNotFoundException;
import cl.duoc.hostly_reservas_service.model.EstadoReserva;
import cl.duoc.hostly_reservas_service.model.Reserva;
import cl.duoc.hostly_reservas_service.repository.EstadoReservaRepository;
import cl.duoc.hostly_reservas_service.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepo;

    @Mock
    private EstadoReservaRepository estadoRepo;

    @Mock
    private ReservaMapper reservaMapper;

    @Mock
    private PropiedadClient propiedadClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    void givenIdUsuarioYPropiedadValidos_whenCrearReserva_thenReturnReservaDTO() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(1));
        inputDto.setFechaFin(LocalDate.now().plusDays(5));
        inputDto.setCantidadMascotas(1);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(true);
        propiedadDTO.setPrecioNoche(45000.0);
        propiedadDTO.setCostoExtraMascota(10000.0);

        Reserva reserva = new Reserva();
        reserva.setIdUsuario(1L);
        reserva.setIdPropiedad(1L);
        reserva.setFechaInicio(inputDto.getFechaInicio());
        reserva.setFechaFin(inputDto.getFechaFin());
        reserva.setCantidadMascotas(1);

        EstadoReserva estadoPendiente = new EstadoReserva();
        estadoPendiente.setId(1L);
        estadoPendiente.setNombre("PENDIENTE");

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setId(10L);
        reservaGuardada.setIdUsuario(1L);
        reservaGuardada.setIdPropiedad(1L);
        reservaGuardada.setFechaInicio(inputDto.getFechaInicio());
        reservaGuardada.setFechaFin(inputDto.getFechaFin());
        reservaGuardada.setCantidadMascotas(1);
        reservaGuardada.setTotalReserva(190000.0);
        reservaGuardada.setEstado(estadoPendiente);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(10L);
        outputDto.setIdUsuario(1L);
        outputDto.setIdPropiedad(1L);
        outputDto.setFechaInicio(inputDto.getFechaInicio());
        outputDto.setFechaFin(inputDto.getFechaFin());
        outputDto.setCantidadMascotas(1);
        outputDto.setTotalReserva(190000.0);
        outputDto.setNombreEstado("PENDIENTE");

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(1L, 3L)).thenReturn(new ArrayList<>());
        when(reservaMapper.toEntity(any(ReservaDTO.class))).thenReturn(reserva);
        when(estadoRepo.findById(1L)).thenReturn(Optional.of(estadoPendiente));
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(reservaMapper.toDTO(reservaGuardada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.crearReserva(inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("PENDIENTE", resultado.getNombreEstado());
        verify(usuarioClient, atMostOnce()).obtenerUsuarioPorId(1L);
        verify(propiedadClient, atMostOnce()).obtenerPropiedadPorId(1L);
        verify(reservaRepo, atMostOnce()).save(any(Reserva.class));
    }

    @Test
    void givenUsuarioInexistente_whenCrearReserva_thenThrowResourceNotFoundException() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenThrow(new RuntimeException("Feign error"));

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.crearReserva(inputDto));
    }

    @Test
    void givenPropiedadInexistente_whenCrearReserva_thenThrowResourceNotFoundException() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenThrow(new RuntimeException("Feign error"));

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.crearReserva(inputDto));
    }

    @Test
    void givenPropiedadNoDisponible_whenCrearReserva_thenThrowRuntimeException() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(false);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);

        // THEN
        assertThrows(RuntimeException.class, () -> reservaService.crearReserva(inputDto));
    }

    @Test
    void givenFechaInicioPasada_whenCrearReserva_thenThrowRuntimeException() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);
        inputDto.setFechaInicio(LocalDate.now().minusDays(1));
        inputDto.setFechaFin(LocalDate.now().plusDays(2));

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(true);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);

        // THEN
        assertThrows(RuntimeException.class, () -> reservaService.crearReserva(inputDto));
    }

    @Test
    void givenOverbooking_whenCrearReserva_thenThrowRuntimeException() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(2));
        inputDto.setFechaFin(LocalDate.now().plusDays(5));

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(true);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(5L);
        reservaExistente.setFechaInicio(LocalDate.now().plusDays(3));
        reservaExistente.setFechaFin(LocalDate.now().plusDays(6));

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(1L, 3L)).thenReturn(List.of(reservaExistente));

        // THEN
        assertThrows(RuntimeException.class, () -> reservaService.crearReserva(inputDto));
    }

    @Test
    void givenIdReserva_whenObtenerReservaPorId_thenReturnReservaDTO() {
        // GIVEN
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setId(id);
        reserva.setIdUsuario(10L);
        reserva.setIdPropiedad(20L);

        ReservaDTO dto = new ReservaDTO();
        dto.setId(id);
        dto.setIdUsuario(10L);
        dto.setIdPropiedad(20L);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(10L);

        PropiedadDTO mockPropiedad = new PropiedadDTO();
        mockPropiedad.setIdPropiedad(20L);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reserva));
        when(reservaMapper.toDTO(reserva)).thenReturn(dto);
        when(usuarioClient.obtenerUsuarioPorId(10L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenReturn(mockPropiedad);

        ReservaDTO resultado = reservaService.obtenerReservaPorId(id);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(mockUsuario, resultado.getUsuario());
        assertEquals(mockPropiedad, resultado.getPropiedad());
    }

    @Test
    void givenIdReservaInexistente_whenObtenerReservaPorId_thenThrowResourceNotFoundException() {
        // GIVEN
        Long id = 99L;

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.obtenerReservaPorId(id));
    }

    @Test
    void whenObtenerTodas_thenReturnListaReservas() {
        // GIVEN
        Reserva r1 = new Reserva();
        r1.setId(1L);
        r1.setIdUsuario(10L);
        r1.setIdPropiedad(20L);

        ReservaDTO d1 = new ReservaDTO();
        d1.setId(1L);
        d1.setIdUsuario(10L);
        d1.setIdPropiedad(20L);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(10L);

        PropiedadDTO mockPropiedad = new PropiedadDTO();
        mockPropiedad.setIdPropiedad(20L);

        // WHEN
        when(reservaRepo.findAll()).thenReturn(List.of(r1));
        when(reservaMapper.toDTO(r1)).thenReturn(d1);
        when(usuarioClient.obtenerUsuarioPorId(10L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenReturn(mockPropiedad);

        List<ReservaDTO> resultado = reservaService.obtenerTodas();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(mockUsuario, resultado.get(0).getUsuario());
    }

    @Test
    void givenIdReserva_whenEliminarReserva_thenSetEstadoCancelada() {
        // GIVEN
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setId(id);

        EstadoReserva estadoCancelada = new EstadoReserva();
        estadoCancelada.setId(3L);
        estadoCancelada.setNombre("CANCELADA");

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reserva));
        when(estadoRepo.findById(3L)).thenReturn(Optional.of(estadoCancelada));
        when(reservaRepo.save(reserva)).thenReturn(reserva);

        reservaService.eliminarReserva(id);

        // THEN
        assertEquals(estadoCancelada, reserva.getEstado());
        verify(reservaRepo, atMostOnce()).save(reserva);
    }

    @Test
    void givenIdReserva_whenConfirmarReserva_thenSetEstadoConfirmada() {
        // GIVEN
        Long id = 1L;
        Reserva reserva = new Reserva();
        reserva.setId(id);

        EstadoReserva estadoConfirmada = new EstadoReserva();
        estadoConfirmada.setId(2L);
        estadoConfirmada.setNombre("CONFIRMADA");

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reserva));
        when(estadoRepo.findById(2L)).thenReturn(Optional.of(estadoConfirmada));
        when(reservaRepo.save(reserva)).thenReturn(reserva);

        reservaService.confirmarReserva(id);

        // THEN
        assertEquals(estadoConfirmada, reserva.getEstado());
        verify(reservaRepo, atMostOnce()).save(reserva);
    }

    @Test
    void givenDatosValidos_whenActualizarReserva_thenReturnReservaDTO() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(10L);
        inputDto.setIdPropiedad(20L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(2));
        inputDto.setFechaFin(LocalDate.now().plusDays(6));
        inputDto.setCantidadMascotas(2);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);
        reservaExistente.setFechaInicio(LocalDate.now().plusDays(1));
        reservaExistente.setFechaFin(LocalDate.now().plusDays(5));
        reservaExistente.setCantidadMascotas(1);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(20L);
        propiedadDTO.setDisponible(true);
        propiedadDTO.setPrecioNoche(45000.0);
        propiedadDTO.setCostoExtraMascota(10000.0);

        Reserva reservaActualizada = new Reserva();
        reservaActualizada.setId(id);
        reservaActualizada.setIdUsuario(10L);
        reservaActualizada.setIdPropiedad(20L);
        reservaActualizada.setFechaInicio(inputDto.getFechaInicio());
        reservaActualizada.setFechaFin(inputDto.getFechaFin());
        reservaActualizada.setCantidadMascotas(2);
        reservaActualizada.setTotalReserva(200000.0);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(id);
        outputDto.setIdUsuario(10L);
        outputDto.setIdPropiedad(20L);
        outputDto.setTotalReserva(200000.0);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(20L, 3L)).thenReturn(new ArrayList<>());
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaActualizada);
        when(reservaMapper.toDTO(reservaActualizada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.actualizarReserva(id, inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(200000.0, resultado.getTotalReserva());
        verify(reservaRepo, atMostOnce()).save(any(Reserva.class));
    }

    @Test
    void givenIdReservaInexistente_whenActualizarReserva_thenThrowResourceNotFoundException() {
        // GIVEN
        Long id = 99L;
        ReservaDTO inputDto = new ReservaDTO();

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.actualizarReserva(id, inputDto));
    }

    @Test
    void givenNuevoUsuarioInexistente_whenActualizarReserva_thenThrowResourceNotFoundException() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(11L);
        inputDto.setIdPropiedad(20L);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(usuarioClient.obtenerUsuarioPorId(11L)).thenThrow(new RuntimeException("Usuario inexistente"));

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.actualizarReserva(id, inputDto));
    }

    @Test
    void givenNuevaPropiedadInexistente_whenActualizarReserva_thenThrowResourceNotFoundException() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(10L);
        inputDto.setIdPropiedad(21L);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(propiedadClient.obtenerPropiedadPorId(21L)).thenThrow(new RuntimeException("Propiedad inexistente"));

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> reservaService.actualizarReserva(id, inputDto));
    }

    @Test
    void givenNuevasFechasConOverbooking_whenActualizarReserva_thenThrowRuntimeException() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(10L);
        inputDto.setIdPropiedad(20L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(2));
        inputDto.setFechaFin(LocalDate.now().plusDays(5));

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);
        reservaExistente.setFechaInicio(LocalDate.now().plusDays(1));
        reservaExistente.setFechaFin(LocalDate.now().plusDays(4));

        Reserva otraReserva = new Reserva();
        otraReserva.setId(2L);
        otraReserva.setFechaInicio(LocalDate.now().plusDays(3));
        otraReserva.setFechaFin(LocalDate.now().plusDays(6));

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenReturn(new PropiedadDTO());
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(20L, 3L)).thenReturn(List.of(reservaExistente, otraReserva));

        // THEN
        assertThrows(RuntimeException.class, () -> reservaService.actualizarReserva(id, inputDto));
    }

    @Test
    void givenNuevasFechasInvalidas_whenActualizarReserva_thenThrowRuntimeException() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(10L);
        inputDto.setIdPropiedad(20L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(5));
        inputDto.setFechaFin(LocalDate.now().plusDays(2));

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenReturn(new PropiedadDTO());
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(20L, 3L)).thenReturn(new ArrayList<>());

        // THEN
        assertThrows(RuntimeException.class, () -> reservaService.actualizarReserva(id, inputDto));
    }

    @Test
    void givenDatosValidosConPreciosPorDefecto_whenCrearReserva_thenReturnReservaDTO() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(1));
        inputDto.setFechaFin(LocalDate.now().plusDays(5));
        inputDto.setCantidadMascotas(1);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(true);
        propiedadDTO.setPrecioNoche(null);
        propiedadDTO.setCostoExtraMascota(null);

        Reserva reserva = new Reserva();
        reserva.setIdUsuario(1L);
        reserva.setIdPropiedad(1L);
        reserva.setFechaInicio(inputDto.getFechaInicio());
        reserva.setFechaFin(inputDto.getFechaFin());
        reserva.setCantidadMascotas(1);

        EstadoReserva estadoPendiente = new EstadoReserva();
        estadoPendiente.setId(1L);
        estadoPendiente.setNombre("PENDIENTE");

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setId(10L);
        reservaGuardada.setIdUsuario(1L);
        reservaGuardada.setIdPropiedad(1L);
        reservaGuardada.setFechaInicio(inputDto.getFechaInicio());
        reservaGuardada.setFechaFin(inputDto.getFechaFin());
        reservaGuardada.setCantidadMascotas(1);
        reservaGuardada.setTotalReserva(190000.0);
        reservaGuardada.setEstado(estadoPendiente);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(10L);
        outputDto.setTotalReserva(190000.0);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(1L, 3L)).thenReturn(new ArrayList<>());
        when(reservaMapper.toEntity(any(ReservaDTO.class))).thenReturn(reserva);
        when(estadoRepo.findById(1L)).thenReturn(Optional.of(estadoPendiente));
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(reservaMapper.toDTO(reservaGuardada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.crearReserva(inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(190000.0, resultado.getTotalReserva());
    }

    @Test
    void givenNochedCeroOMenos_whenCrearReserva_thenThrowRuntimeException() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(5));
        inputDto.setFechaFin(LocalDate.now().plusDays(2));

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(true);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(1L, 3L)).thenReturn(new ArrayList<>());

        // THEN
        assertThrows(RuntimeException.class, () -> reservaService.crearReserva(inputDto));
    }

    @Test
    void givenCambioDeUsuarioYPropiedadExitoso_whenActualizarReserva_thenReturnReservaDTO() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(11L);
        inputDto.setIdPropiedad(21L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(2));
        inputDto.setFechaFin(LocalDate.now().plusDays(6));
        inputDto.setCantidadMascotas(1);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);
        reservaExistente.setFechaInicio(LocalDate.now().plusDays(1));
        reservaExistente.setFechaFin(LocalDate.now().plusDays(5));
        reservaExistente.setCantidadMascotas(1);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(11L);

        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(21L);
        propiedadDTO.setDisponible(true);
        propiedadDTO.setPrecioNoche(45000.0);
        propiedadDTO.setCostoExtraMascota(10000.0);

        Reserva reservaActualizada = new Reserva();
        reservaActualizada.setId(id);
        reservaActualizada.setIdUsuario(11L);
        reservaActualizada.setIdPropiedad(21L);
        reservaActualizada.setFechaInicio(inputDto.getFechaInicio());
        reservaActualizada.setFechaFin(inputDto.getFechaFin());
        reservaActualizada.setCantidadMascotas(1);
        reservaActualizada.setTotalReserva(190000.0);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(id);
        outputDto.setIdUsuario(11L);
        outputDto.setIdPropiedad(21L);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(usuarioClient.obtenerUsuarioPorId(11L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(21L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(21L, 3L)).thenReturn(new ArrayList<>());
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaActualizada);
        when(reservaMapper.toDTO(reservaActualizada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.actualizarReserva(id, inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(11L, resultado.getIdUsuario());
        assertEquals(21L, resultado.getIdPropiedad());
    }

    @Test
    void givenFalloFeignPrecios_whenActualizarReserva_thenReturnReservaDTO() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(10L);
        inputDto.setIdPropiedad(20L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(2));
        inputDto.setFechaFin(LocalDate.now().plusDays(6));
        inputDto.setCantidadMascotas(1);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);
        reservaExistente.setFechaInicio(LocalDate.now().plusDays(1));
        reservaExistente.setFechaFin(LocalDate.now().plusDays(5));
        reservaExistente.setCantidadMascotas(1);

        Reserva reservaActualizada = new Reserva();
        reservaActualizada.setId(id);
        reservaActualizada.setIdUsuario(10L);
        reservaActualizada.setIdPropiedad(20L);
        reservaActualizada.setFechaInicio(inputDto.getFechaInicio());
        reservaActualizada.setFechaFin(inputDto.getFechaFin());
        reservaActualizada.setCantidadMascotas(1);
        reservaActualizada.setTotalReserva(190000.0);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(id);
        outputDto.setTotalReserva(190000.0);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenThrow(new RuntimeException("Feign error"));
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(20L, 3L)).thenReturn(new ArrayList<>());
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaActualizada);
        when(reservaMapper.toDTO(reservaActualizada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.actualizarReserva(id, inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(190000.0, resultado.getTotalReserva());
    }

    @Test
    void givenPropiedadDisponibleNull_whenCrearReserva_thenReturnReservaDTO() {
        // GIVEN
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(1L);
        inputDto.setIdPropiedad(1L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(1));
        inputDto.setFechaFin(LocalDate.now().plusDays(5));
        inputDto.setCantidadMascotas(1);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setId(1L);

        // disponible = null para forzar la rama del condicional correspondiente
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(1L);
        propiedadDTO.setDisponible(null);
        propiedadDTO.setPrecioNoche(45000.0);
        propiedadDTO.setCostoExtraMascota(10000.0);

        Reserva reserva = new Reserva();
        reserva.setIdUsuario(1L);
        reserva.setIdPropiedad(1L);
        reserva.setFechaInicio(inputDto.getFechaInicio());
        reserva.setFechaFin(inputDto.getFechaFin());
        reserva.setCantidadMascotas(1);

        EstadoReserva estadoPendiente = new EstadoReserva();
        estadoPendiente.setId(1L);
        estadoPendiente.setNombre("PENDIENTE");

        Reserva reservaGuardada = new Reserva();
        reservaGuardada.setId(10L);
        reservaGuardada.setTotalReserva(190000.0);
        reservaGuardada.setEstado(estadoPendiente);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(10L);
        outputDto.setTotalReserva(190000.0);

        // WHEN
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(mockUsuario);
        when(propiedadClient.obtenerPropiedadPorId(1L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(1L, 3L)).thenReturn(new ArrayList<>());
        when(reservaMapper.toEntity(any(ReservaDTO.class))).thenReturn(reserva);
        when(estadoRepo.findById(1L)).thenReturn(Optional.of(estadoPendiente));
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaGuardada);
        when(reservaMapper.toDTO(reservaGuardada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.crearReserva(inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(190000.0, resultado.getTotalReserva());
    }

    @Test
    void givenDatosValidosConPreciosNull_whenActualizarReserva_thenReturnReservaDTO() {
        // GIVEN
        Long id = 1L;
        ReservaDTO inputDto = new ReservaDTO();
        inputDto.setIdUsuario(10L);
        inputDto.setIdPropiedad(20L);
        inputDto.setFechaInicio(LocalDate.now().plusDays(2));
        inputDto.setFechaFin(LocalDate.now().plusDays(6));
        inputDto.setCantidadMascotas(1);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(id);
        reservaExistente.setIdUsuario(10L);
        reservaExistente.setIdPropiedad(20L);
        reservaExistente.setFechaInicio(LocalDate.now().plusDays(1));
        reservaExistente.setFechaFin(LocalDate.now().plusDays(5));
        reservaExistente.setCantidadMascotas(1);

        // Precios null en actualización
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setIdPropiedad(20L);
        propiedadDTO.setDisponible(true);
        propiedadDTO.setPrecioNoche(null);
        propiedadDTO.setCostoExtraMascota(null);

        Reserva reservaActualizada = new Reserva();
        reservaActualizada.setId(id);
        reservaActualizada.setTotalReserva(190000.0);

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(id);
        outputDto.setTotalReserva(190000.0);

        // WHEN
        when(reservaRepo.findById(id)).thenReturn(Optional.of(reservaExistente));
        when(propiedadClient.obtenerPropiedadPorId(20L)).thenReturn(propiedadDTO);
        when(reservaRepo.findByIdPropiedadAndEstadoIdNot(20L, 3L)).thenReturn(new ArrayList<>());
        when(reservaRepo.save(any(Reserva.class))).thenReturn(reservaActualizada);
        when(reservaMapper.toDTO(reservaActualizada)).thenReturn(outputDto);

        ReservaDTO resultado = reservaService.actualizarReserva(id, inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(190000.0, resultado.getTotalReserva());
    }
}
