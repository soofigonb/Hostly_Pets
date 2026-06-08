package cl.duoc.hostly_reservas_service.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.duoc.hostly_reservas_service.dto.ReservaDTO;
import cl.duoc.hostly_reservas_service.exceptions.ResourceNotFoundException;
import cl.duoc.hostly_reservas_service.services.ReservaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;

    @Test
    void whenListarReservas_thenReturnOk() throws Exception {
        // GIVEN
        ReservaDTO d1 = new ReservaDTO();
        d1.setId(1L);
        d1.setIdUsuario(10L);
        d1.setIdPropiedad(20L);

        List<ReservaDTO> reservas = List.of(d1);

        // WHEN
        when(reservaService.obtenerTodas()).thenReturn(reservas);

        // THEN
        mockMvc.perform(get("/api/v1/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void givenIdReserva_whenObtenerPorId_thenReturnOk() throws Exception {
        // GIVEN
        Long id = 1L;
        ReservaDTO dto = new ReservaDTO();
        dto.setId(id);
        dto.setIdUsuario(10L);

        // WHEN
        when(reservaService.obtenerReservaPorId(id)).thenReturn(dto);

        // THEN
        mockMvc.perform(get("/api/v1/reservas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.idUsuario").value(10L));
    }

    @Test
    void givenIdReservaInexistente_whenObtenerPorId_thenReturnNotFound() throws Exception {
        // GIVEN
        Long id = 99L;

        // WHEN
        when(reservaService.obtenerReservaPorId(id)).thenThrow(new ResourceNotFoundException("Reserva no encontrada"));

        // THEN
        mockMvc.perform(get("/api/v1/reservas/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCrearReserva_thenReturnCreated() throws Exception {
        // GIVEN
        String fechaInicioStr = LocalDate.now().plusDays(1).toString();
        String fechaFinStr = LocalDate.now().plusDays(5).toString();

        String bodyJson = String.format(
            "{\"idUsuario\":10,\"idPropiedad\":20,\"fechaInicio\":\"%s\",\"fechaFin\":\"%s\",\"cantidadMascotas\":1,\"tipoMascota\":\"Perro\",\"tamanoMascota\":\"Mediano\"}",
            fechaInicioStr, fechaFinStr
        );

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(1L);
        outputDto.setIdUsuario(10L);
        outputDto.setIdPropiedad(20L);

        // WHEN
        when(reservaService.crearReserva(any(ReservaDTO.class))).thenReturn(outputDto);

        // THEN
        mockMvc.perform(post("/api/v1/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idUsuario").value(10L));
    }

    @Test
    void whenCrearReservaInvalida_thenReturnBadRequest() throws Exception {
        // GIVEN (idUsuario e idPropiedad nulos, que violan @NotNull)
        String bodyJson = "{\"idUsuario\":null,\"idPropiedad\":null,\"cantidadMascotas\":0}";

        // THEN
        mockMvc.perform(post("/api/v1/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenActualizarReserva_thenReturnOk() throws Exception {
        // GIVEN
        Long id = 1L;
        String fechaInicioStr = LocalDate.now().plusDays(2).toString();
        String fechaFinStr = LocalDate.now().plusDays(6).toString();

        String bodyJson = String.format(
            "{\"idUsuario\":10,\"idPropiedad\":20,\"fechaInicio\":\"%s\",\"fechaFin\":\"%s\",\"cantidadMascotas\":2}",
            fechaInicioStr, fechaFinStr
        );

        ReservaDTO outputDto = new ReservaDTO();
        outputDto.setId(id);
        outputDto.setIdUsuario(10L);
        outputDto.setIdPropiedad(20L);

        // WHEN
        when(reservaService.actualizarReserva(eq(id), any(ReservaDTO.class))).thenReturn(outputDto);

        // THEN
        mockMvc.perform(put("/api/v1/reservas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void whenEliminarReserva_thenReturnNoContent() throws Exception {
        // GIVEN
        Long id = 1L;

        // WHEN
        doNothing().when(reservaService).eliminarReserva(id);

        // THEN
        mockMvc.perform(delete("/api/v1/reservas/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenConfirmarReserva_thenReturnOk() throws Exception {
        // GIVEN
        Long id = 1L;

        // WHEN
        doNothing().when(reservaService).confirmarReserva(id);

        // THEN
        mockMvc.perform(put("/api/v1/reservas/{id}/confirmar", id))
                .andExpect(status().isOk());
    }
}
