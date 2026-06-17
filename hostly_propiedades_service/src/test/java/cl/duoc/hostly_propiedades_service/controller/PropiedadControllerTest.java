package cl.duoc.hostly_propiedades_service.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.duoc.hostly_propiedades_service.dto.PropiedadRequestDTO;
import cl.duoc.hostly_propiedades_service.model.Propiedad;
import cl.duoc.hostly_propiedades_service.model.TamanoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoMascota;
import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;
import cl.duoc.hostly_propiedades_service.service.PropiedadService;

// Aisla el controlador para probar solo la lógica REST
@WebMvcTest(PropiedadController.class)
public class PropiedadControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite ejecutar llamadas GET/POST/PUT/DELETE simuladas

    @MockitoBean // Mock del servicio para no depender de la base de datos real
    private PropiedadService propiedadService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Propiedad crearPropiedad() {
        TipoPropiedad tipoPropiedad = new TipoPropiedad(1L, "Casa");
        TipoMascota tipoMascota = new TipoMascota(1L, "Perro");
        TamanoMascota tamanoMascota = new TamanoMascota(1L, "Mediano");

        Propiedad propiedad = new Propiedad();
        propiedad.setIdPropiedad(1L);
        propiedad.setIdAnfitrion(10L);
        propiedad.setTitulo("Casa pet friendly");
        propiedad.setDescripcion("Casa amplia para mascotas");
        propiedad.setDireccion("Calle 123");
        propiedad.setCiudad("Quilpué");
        propiedad.setPrecioNoche(50000.0);
        propiedad.setTienePatio(true);
        propiedad.setCostoExtraMascota(5000.0);
        propiedad.setDisponible(true);
        propiedad.setTipoPropiedad(tipoPropiedad);
        propiedad.setTipoMascota(tipoMascota);
        propiedad.setTamanoMascota(tamanoMascota);

        return propiedad;
    }

    private PropiedadRequestDTO crearRequestDTO() {
        PropiedadRequestDTO dto = new PropiedadRequestDTO();
        dto.setIdAnfitrion(10L);
        dto.setTitulo("Casa pet friendly");
        dto.setDescripcion("Casa amplia para mascotas");
        dto.setDireccion("Calle 123");
        dto.setCiudad("Quilpué");
        dto.setPrecioNoche(50000.0);
        dto.setTienePatio(true);
        dto.setCostoExtraMascota(5000.0);
        dto.setDisponible(true);
        dto.setIdTipoPropiedad(1L);
        dto.setIdTipoMascota(1L);
        dto.setIdTamanoMascota(1L);
        return dto;
    }

    @Test
    void whenObtenerPropiedades_thenReturnOk() throws Exception {
        when(propiedadService.obtenerPropiedades()).thenReturn(List.of(crearPropiedad()));

        mockMvc.perform(get("/api/v1/propiedades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idPropiedad").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Casa pet friendly"));
    }

    @Test
    void whenObtenerPropiedadPorId_thenReturnOk() throws Exception {
        when(propiedadService.obtenerPropiedadPorId(1L)).thenReturn(crearPropiedad());

        mockMvc.perform(get("/api/v1/propiedades/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPropiedad").value(1L))
                .andExpect(jsonPath("$.ciudad").value("Quilpué"));
    }

    @Test
    void whenObtenerPropiedadPorIdNoExiste_thenReturnBadRequest() throws Exception {
        when(propiedadService.obtenerPropiedadPorId(99L))
                .thenThrow(new IllegalArgumentException("Propiedad no encontrada"));

        mockMvc.perform(get("/api/v1/propiedades/{id}", 99L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCrearPropiedad_thenReturnOk() throws Exception {
        PropiedadRequestDTO request = crearRequestDTO();

        when(propiedadService.crearPropiedad(any(PropiedadRequestDTO.class)))
                .thenReturn(crearPropiedad());

        mockMvc.perform(post("/api/v1/propiedades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Casa pet friendly"));
    }

    @Test
    void whenCrearPropiedadInvalida_thenReturnBadRequest() throws Exception {
        PropiedadRequestDTO request = new PropiedadRequestDTO();

        mockMvc.perform(post("/api/v1/propiedades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenActualizarPropiedad_thenReturnOk() throws Exception {
        PropiedadRequestDTO request = crearRequestDTO();

        when(propiedadService.actualizarPropiedad(eq(1L), any(PropiedadRequestDTO.class)))
                .thenReturn(crearPropiedad());

        mockMvc.perform(put("/api/v1/propiedades/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPropiedad").value(1L));
    }

    @Test
    void whenEliminarPropiedad_thenReturnOk() throws Exception {
        doNothing().when(propiedadService).eliminarPropiedad(1L);

        mockMvc.perform(delete("/api/v1/propiedades/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenObtenerPropiedadesPorCiudad_thenReturnOk() throws Exception {
        when(propiedadService.obtenerPropiedadesPorCiudad("Quilpué"))
                .thenReturn(List.of(crearPropiedad()));

        mockMvc.perform(get("/api/v1/propiedades/ciudad/{ciudad}", "Quilpué"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void whenObtenerPropiedadesDisponibles_thenReturnOk() throws Exception {
        when(propiedadService.obtenerPropiedadesDisponibles())
                .thenReturn(List.of(crearPropiedad()));

        mockMvc.perform(get("/api/v1/propiedades/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void whenObtenerPropiedadesPorAnfitrion_thenReturnOk() throws Exception {
        when(propiedadService.obtenerPropiedadesPorAnfitrion(10L))
                .thenReturn(List.of(crearPropiedad()));

        mockMvc.perform(get("/api/v1/propiedades/anfitrion/{idAnfitrion}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}