package cl.duoc.hostly_reservas_service.controller;

import cl.duoc.hostly_reservas_service.dto.ReservaDTO;
import cl.duoc.hostly_reservas_service.services.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor 
public class ReservaController {

    private final ReservaService reservaService;

    // Obtener todas las reservas 
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> reservas = reservaService.obtenerTodas();
        return ResponseEntity.ok(reservas); 
    }

    //Obtener una reserva por su ID (consultas detalladas)
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> obtenerPorId(@PathVariable Long id) {
        ReservaDTO reserva = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        ReservaDTO nuevaReserva = reservaService.crearReserva(reservaDTO);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED); 
    }
}