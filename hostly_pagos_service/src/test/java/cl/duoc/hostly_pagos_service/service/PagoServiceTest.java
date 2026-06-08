package cl.duoc.hostly_pagos_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.hostly_pagos_service.client.ReservaClient;
import cl.duoc.hostly_pagos_service.dto.PagoDTO;
import cl.duoc.hostly_pagos_service.dto.PagoMapper;
import cl.duoc.hostly_pagos_service.dto.ReservaDTO;
import cl.duoc.hostly_pagos_service.exceptions.ResourceNotFoundException;
import cl.duoc.hostly_pagos_service.model.EstadoPago;
import cl.duoc.hostly_pagos_service.model.MetodoPago;
import cl.duoc.hostly_pagos_service.model.Pago;
import cl.duoc.hostly_pagos_service.repository.EstadoPagoRepository;
import cl.duoc.hostly_pagos_service.repository.MetodoPagoRepository;
import cl.duoc.hostly_pagos_service.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepo;

    @Mock
    private EstadoPagoRepository estadoRepo;

    @Mock
    private PagoMapper pagoMapper;

    @Mock
    private ReservaClient reservaClient;

    @Mock
    private MetodoPagoRepository metodoPagoRepo;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void givenIdReservaNull_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(null);

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenReservaInexistente_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenThrow(new RuntimeException("Feign error"));

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenReservaYaPagada_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("PENDIENTE");

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);
        when(pagoRepo.existsByIdReservaAndEstadoId(1L, 2L)).thenReturn(true);

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenReservaNoPendiente_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("CONFIRMADA"); // No pendiente

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenMontoInvalido_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);
        inputDto.setMonto(0.0); // Monto <= 0

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("PENDIENTE");
        mockReserva.setTotalReserva(0.0); // Fuerza a monto 0

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);
        when(pagoRepo.existsByIdReservaAndEstadoId(1L, 2L)).thenReturn(false);

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenMetodoPagoInvalido_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);
        inputDto.setMonto(100.0);
        inputDto.setMetodoPago("Invalido");

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("PENDIENTE");
        mockReserva.setTotalReserva(100.0);

        Pago pago = new Pago();

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);
        when(pagoRepo.existsByIdReservaAndEstadoId(1L, 2L)).thenReturn(false);
        when(pagoMapper.toEntity(inputDto)).thenReturn(pago);
        when(metodoPagoRepo.findByNombreIgnoreCase("Invalido")).thenReturn(Optional.empty());

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenEstadoPagoNoConfigurado_whenProcesarPago_thenThrowRuntimeException() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);
        inputDto.setMonto(100.0);
        inputDto.setMetodoPago("Efectivo");

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("PENDIENTE");
        mockReserva.setTotalReserva(100.0);

        Pago pago = new Pago();
        MetodoPago metodo = new MetodoPago();

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);
        when(pagoRepo.existsByIdReservaAndEstadoId(1L, 2L)).thenReturn(false);
        when(pagoMapper.toEntity(inputDto)).thenReturn(pago);
        when(metodoPagoRepo.findByNombreIgnoreCase("Efectivo")).thenReturn(Optional.of(metodo));
        when(estadoRepo.findById(2L)).thenReturn(Optional.empty()); // Fallo de BD

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.procesarPago(inputDto));
    }

    @Test
    void givenDatosValidos_whenProcesarPago_thenReturnPagoDTO() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);
        inputDto.setMonto(100.0);
        inputDto.setMetodoPago("Efectivo");

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("PENDIENTE");
        mockReserva.setTotalReserva(100.0);

        Pago pago = new Pago();
        MetodoPago metodo = new MetodoPago();
        EstadoPago estado = new EstadoPago();

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(10L);

        PagoDTO outputDto = new PagoDTO();
        outputDto.setId(10L);

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);
        when(pagoRepo.existsByIdReservaAndEstadoId(1L, 2L)).thenReturn(false);
        when(pagoMapper.toEntity(inputDto)).thenReturn(pago);
        when(metodoPagoRepo.findByNombreIgnoreCase("Efectivo")).thenReturn(Optional.of(metodo));
        when(estadoRepo.findById(2L)).thenReturn(Optional.of(estado));
        when(pagoRepo.save(any(Pago.class))).thenReturn(pagoGuardado);
        when(pagoMapper.toDTO(pagoGuardado)).thenReturn(outputDto);

        PagoDTO resultado = pagoService.procesarPago(inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        verify(reservaClient, atMostOnce()).confirmarReserva(1L);
    }

    @Test
    void givenDatosValidosYFalloNotificacion_whenProcesarPago_thenReturnPagoDTO() {
        // GIVEN
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L);
        inputDto.setMonto(100.0);
        inputDto.setMetodoPago("Efectivo");

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(1L);
        mockReserva.setNombreEstado("PENDIENTE");
        mockReserva.setTotalReserva(100.0);

        Pago pago = new Pago();
        MetodoPago metodo = new MetodoPago();
        EstadoPago estado = new EstadoPago();

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(10L);

        PagoDTO outputDto = new PagoDTO();
        outputDto.setId(10L);

        // WHEN
        when(reservaClient.obtenerReservaPorId(1L)).thenReturn(mockReserva);
        when(pagoRepo.existsByIdReservaAndEstadoId(1L, 2L)).thenReturn(false);
        when(pagoMapper.toEntity(inputDto)).thenReturn(pago);
        when(metodoPagoRepo.findByNombreIgnoreCase("Efectivo")).thenReturn(Optional.of(metodo));
        when(estadoRepo.findById(2L)).thenReturn(Optional.of(estado));
        when(pagoRepo.save(any(Pago.class))).thenReturn(pagoGuardado);
        when(pagoMapper.toDTO(pagoGuardado)).thenReturn(outputDto);
        // Lanzamos excepcion en confirmación para asegurar que el catch funciona
        doThrow(new RuntimeException("Feign error")).when(reservaClient).confirmarReserva(1L);

        PagoDTO resultado = pagoService.procesarPago(inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
    }

    @Test
    void whenObtenerTodos_thenReturnListaPagos() {
        // GIVEN
        Pago p1 = new Pago();
        p1.setId(1L);
        PagoDTO d1 = new PagoDTO();
        d1.setId(1L);

        // WHEN
        when(pagoRepo.findAll()).thenReturn(List.of(p1));
        when(pagoMapper.toDTO(p1)).thenReturn(d1);

        List<PagoDTO> resultado = pagoService.obtenerTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void givenIdReserva_whenObtenerPagosPorReserva_thenReturnListaPagos() {
        // GIVEN
        Long idReserva = 1L;
        Pago p1 = new Pago();
        p1.setId(1L);
        PagoDTO d1 = new PagoDTO();
        d1.setId(1L);

        // WHEN
        when(pagoRepo.findByIdReserva(idReserva)).thenReturn(List.of(p1));
        when(pagoMapper.toDTO(p1)).thenReturn(d1);

        List<PagoDTO> resultado = pagoService.obtenerPagosPorReserva(idReserva);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void givenIdPagoInexistente_whenActualizarPago_thenThrowResourceNotFoundException() {
        // GIVEN
        Long id = 99L;
        PagoDTO inputDto = new PagoDTO();

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> pagoService.actualizarPago(id, inputDto));
    }

    @Test
    void givenIdPagoExistenteConNuevaReservaValida_whenActualizarPago_thenReturnPagoDTO() {
        // GIVEN
        Long id = 1L;
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(2L); // Cambia de 1L a 2L
        inputDto.setMonto(150.0);

        Pago pagoExistente = new Pago();
        pagoExistente.setId(id);
        pagoExistente.setIdReserva(1L);

        ReservaDTO mockReserva = new ReservaDTO();
        mockReserva.setId(2L);

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(id);

        PagoDTO outputDto = new PagoDTO();
        outputDto.setId(id);

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.of(pagoExistente));
        when(reservaClient.obtenerReservaPorId(2L)).thenReturn(mockReserva);
        when(pagoRepo.save(any(Pago.class))).thenReturn(pagoGuardado);
        when(pagoMapper.toDTO(pagoGuardado)).thenReturn(outputDto);

        PagoDTO resultado = pagoService.actualizarPago(id, inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void givenIdPagoExistenteConNuevaReservaInexistente_whenActualizarPago_thenThrowRuntimeException() {
        // GIVEN
        Long id = 1L;
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(2L); // Cambia de 1L a 2L

        Pago pagoExistente = new Pago();
        pagoExistente.setId(id);
        pagoExistente.setIdReserva(1L);

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.of(pagoExistente));
        when(reservaClient.obtenerReservaPorId(2L)).thenThrow(new RuntimeException("Feign error"));

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.actualizarPago(id, inputDto));
    }

    @Test
    void givenIdPagoExistenteSinCambioReservaYMetodoInvalido_whenActualizarPago_thenThrowRuntimeException() {
        // GIVEN
        Long id = 1L;
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L); // No cambia
        inputDto.setMetodoPago("Invalido");

        Pago pagoExistente = new Pago();
        pagoExistente.setId(id);
        pagoExistente.setIdReserva(1L);

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.of(pagoExistente));
        when(metodoPagoRepo.findByNombreIgnoreCase("Invalido")).thenReturn(Optional.empty());

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.actualizarPago(id, inputDto));
    }

    @Test
    void givenIdPagoExistenteSinCambioReservaYMetodoValido_whenActualizarPago_thenReturnPagoDTO() {
        // GIVEN
        Long id = 1L;
        PagoDTO inputDto = new PagoDTO();
        inputDto.setIdReserva(1L); // No cambia
        inputDto.setMetodoPago("Efectivo");

        Pago pagoExistente = new Pago();
        pagoExistente.setId(id);
        pagoExistente.setIdReserva(1L);

        MetodoPago metodo = new MetodoPago();
        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(id);

        PagoDTO outputDto = new PagoDTO();
        outputDto.setId(id);

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.of(pagoExistente));
        when(metodoPagoRepo.findByNombreIgnoreCase("Efectivo")).thenReturn(Optional.of(metodo));
        when(pagoRepo.save(any(Pago.class))).thenReturn(pagoGuardado);
        when(pagoMapper.toDTO(pagoGuardado)).thenReturn(outputDto);

        PagoDTO resultado = pagoService.actualizarPago(id, inputDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    void givenIdPagoInexistente_whenEliminarPago_thenThrowResourceNotFoundException() {
        // GIVEN
        Long id = 99L;

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> pagoService.eliminarPago(id));
    }

    @Test
    void givenEstadoAnuladoInexistente_whenEliminarPago_thenThrowRuntimeException() {
        // GIVEN
        Long id = 1L;
        Pago pago = new Pago();
        pago.setId(id);

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.of(pago));
        when(estadoRepo.findById(3L)).thenReturn(Optional.empty()); // No configurado

        // THEN
        assertThrows(RuntimeException.class, () -> pagoService.eliminarPago(id));
    }

    @Test
    void givenIdPagoExistente_whenEliminarPago_thenSetEstadoAnulado() {
        // GIVEN
        Long id = 1L;
        Pago pago = new Pago();
        pago.setId(id);

        EstadoPago estadoAnulado = new EstadoPago();
        estadoAnulado.setId(3L);
        estadoAnulado.setNombre("ANULADO");

        // WHEN
        when(pagoRepo.findById(id)).thenReturn(Optional.of(pago));
        when(estadoRepo.findById(3L)).thenReturn(Optional.of(estadoAnulado));
        when(pagoRepo.save(pago)).thenReturn(pago);

        pagoService.eliminarPago(id);

        // THEN
        assertEquals(estadoAnulado, pago.getEstado());
        verify(pagoRepo, atMostOnce()).save(pago);
    }
}
