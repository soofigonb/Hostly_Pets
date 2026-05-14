package cl.duoc.hostly_propiedades_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.hostly_propiedades_service.model.TipoMascota;
import cl.duoc.hostly_propiedades_service.repository.TipoMascotaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoMascotaService {

    private static final Logger logger = LoggerFactory.getLogger(TipoMascotaService.class);

    private final TipoMascotaRepository tipoMascotaRepository;

    // Busca un tipo de mascota por ID
    public TipoMascota obtenerTipoMascotaPorId(Long id) {

        logger.info("Buscando tipo de mascota con id {}", id);

        return tipoMascotaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tipo de mascota no encontrado con id {}", id);
                    return new IllegalArgumentException("Tipo de mascota no encontrado");
                });
    }
}
