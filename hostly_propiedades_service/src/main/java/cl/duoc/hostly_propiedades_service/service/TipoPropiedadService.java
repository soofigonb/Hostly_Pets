package cl.duoc.hostly_propiedades_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.hostly_propiedades_service.model.TipoPropiedad;
import cl.duoc.hostly_propiedades_service.repository.TipoPropiedadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoPropiedadService {

    private static final Logger logger = LoggerFactory.getLogger(TipoPropiedadService.class);

    private final TipoPropiedadRepository tipoPropiedadRepository;

    // Busca un tipo de propiedad por ID
    public TipoPropiedad obtenerTipoPropiedadPorId(Long id) {

        logger.info("Buscando tipo de propiedad con id {}", id);

        return tipoPropiedadRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Tipo de propiedad no encontrado con id {}", id);
                    return new IllegalArgumentException("Tipo de propiedad no encontrado");
                });
    }
}