package cl.duoc.hostly_pagos_service.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.duoc.hostly_pagos_service.dto.PagoDTO;
import cl.duoc.hostly_pagos_service.exceptions.ResourceNotFoundException;
import cl.duoc.hostly_pagos_service.service.PagoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @Test
    void whenListarTodos_thenReturnOk() throws Exception {
        // GIVEN
        PagoDTO d1 = new PagoDTO();
        d1.setId(1L);
        d1.setIdReserva(10L);
        d1.setMonto(150000.0);
        d1.setMetodoPago("Efectivo");

        List<PagoDTO> pagos = List.of(d1);

        // WHEN
        when(pagoService.obtenerTodos()).thenReturn(pagos);

        // THEN
        mockMvc.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].monto").value(150000.0));
    }

    @Test
    void givenIdReserva_whenBuscarPorReserva_thenReturnOk() throws Exception {
        // GIVEN
        Long idReserva = 10L;
        PagoDTO d1 = new PagoDTO();
        d1.setId(1L);
        d1.setIdReserva(idReserva);
        d1.setMonto(150000.0);

        List<PagoDTO> pagos = List.of(d1);

        // WHEN
        when(pagoService.obtenerPagosPorReserva(idReserva)).thenReturn(pagos);

        // THEN
        mockMvc.perform(get("/api/v1/pagos/reserva/{idReserva}", idReserva))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idReserva").value(idReserva));
    }

    @Test
    void whenRealizarPago_thenReturnCreated() throws Exception {
        // GIVEN
        String bodyJson = "{\"idReserva\":10,\"monto\":150000.0,\"metodoPago\":\"Efectivo\"}";
        PagoDTO outputDto = new PagoDTO();
        outputDto.setId(1L);
        outputDto.setIdReserva(10L);
        outputDto.setMonto(150000.0);
        outputDto.setMetodoPago("Efectivo");

        // WHEN
        when(pagoService.procesarPago(any(PagoDTO.class))).thenReturn(outputDto);

        // THEN
        mockMvc.perform(post("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.monto").value(150000.0));
    }

    @Test
    void whenRealizarPagoInvalido_thenReturnBadRequest() throws Exception {
        // GIVEN (Monto <= 0 que viola @Positive, e idReserva nulo que viola @NotNull)
        String bodyJson = "{\"idReserva\":null,\"monto\":-10.0,\"metodoPago\":\"\"}";

        // THEN
        mockMvc.perform(post("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenActualizarPago_thenReturnOk() throws Exception {
        // GIVEN
        Long id = 1L;
        String bodyJson = "{\"idReserva\":10,\"monto\":120000.0,\"metodoPago\":\"Tarjeta\"}";
        PagoDTO outputDto = new PagoDTO();
        outputDto.setId(id);
        outputDto.setMonto(120000.0);

        // WHEN
        when(pagoService.actualizarPago(eq(id), any(PagoDTO.class))).thenReturn(outputDto);

        // THEN
        mockMvc.perform(put("/api/v1/pagos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.monto").value(120000.0));
    }

    @Test
    void givenIdPagoInexistente_whenActualizarPago_thenReturnNotFound() throws Exception {
        // GIVEN
        Long id = 99L;
        String bodyJson = "{\"idReserva\":10,\"monto\":120000.0,\"metodoPago\":\"Tarjeta\"}";

        // WHEN
        when(pagoService.actualizarPago(eq(id), any(PagoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Pago no encontrado"));

        // THEN
        mockMvc.perform(put("/api/v1/pagos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenEliminarPago_thenReturnNoContent() throws Exception {
        // GIVEN
        Long id = 1L;

        // WHEN
        doNothing().when(pagoService).eliminarPago(id);

        // THEN
        mockMvc.perform(delete("/api/v1/pagos/{id}", id))
                .andExpect(status().isNoContent());
    }
}
