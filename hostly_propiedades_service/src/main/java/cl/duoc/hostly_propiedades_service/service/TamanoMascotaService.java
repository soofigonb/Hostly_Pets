package cl.duoc.hostly_propiedades_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.hostly_propiedades_service.model.TamanoMascota;
import cl.duoc.hostly_propiedades_service.repository.TamanoMascotaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TamanoMascotaService {

    private static final Logger logger = LoggerFactory.getLogger(TamanoMascotaService.class);

    private final TamanoMascotaRepository tamanoMascotaRepository;

    // Busca un tamaño de mascota por ID
    public TamanoMascota obtenerTamanoMascotaPorId(Long id) {

        logger.info("Buscando tamaño de mascota con id {}", id);

        return tamanoMascotaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tamaño de mascota no encontrado con id {}", id);
                    return new IllegalArgumentException("Tamaño de mascota no encontrado");
                });
    }
}