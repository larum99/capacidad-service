package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadServicePort;
import com.onclass.capacidad.domain.constants.Constants;
import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.PageResult;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadListDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CapacidadUseCase implements CapacidadServicePort {

    private final CapacidadPersistencePort capacidadPersistencePort;
    private final TecnologiaClientPort tecnologiaClientPort;

    public CapacidadUseCase(CapacidadPersistencePort capacidadPersistencePort,
                            TecnologiaClientPort tecnologiaClientPort) {
        this.capacidadPersistencePort = capacidadPersistencePort;
        this.tecnologiaClientPort = tecnologiaClientPort;
    }

    // 🔹 HU1 - Registrar capacidad
    @Override
    public Mono<Capacidad> registrarCapacidad(Capacidad capacidad, String messageId) {
        if (capacidad.nombre() == null || capacidad.nombre().isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_NOMBRE_REQUIRED));
        }
        if (capacidad.nombre().length() > Constants.MAX_NOMBRE_CAPACIDAD) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_NOMBRE_TOO_LONG));
        }

        if (capacidad.descripcion() == null || capacidad.descripcion().isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_DESCRIPCION_REQUIRED));
        }
        if (capacidad.descripcion().length() > Constants.MAX_DESCRIPCION_CAPACIDAD) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_DESCRIPCION_TOO_LONG));
        }

        List<Long> tecnologias = capacidad.tecnologias();
        if (tecnologias == null || tecnologias.size() < Constants.MIN_TECNOLOGIAS) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_MIN_TECNOLOGIAS));
        }
        if (tecnologias.size() > Constants.MAX_TECNOLOGIAS) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_MAX_TECNOLOGIAS));
        }

        Set<Long> set = new HashSet<>(tecnologias);
        if (set.size() != tecnologias.size()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_TECNOLOGIAS_DUPLICADAS));
        }

        return capacidadPersistencePort.existByNombre(capacidad.nombre())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_ALREADY_EXISTS));
                    }

                    return capacidadPersistencePort.saveCapacidad(capacidad)
                            .flatMap(saved -> tecnologiaClientPort
                                    .associateCapacidadWithTecnologias(saved.id(), tecnologias)
                                    .thenReturn(saved)
                            );
                });
    }

    // 🔹 HU3 - Listar capacidades con tecnologías
    @Override
    public Mono<PageResult<CapacidadListDTO>> listarCapacidades(CapacidadCriteria criteria) {
        return capacidadPersistencePort.findAll(criteria)
                .flatMap(page -> {
                    // ✅ usamos los getters correctos
                    Flux<CapacidadListDTO> enrichedFlux = Flux.fromIterable(page.getContent())
                            .flatMap(dto ->
                                    tecnologiaClientPort.findTecnologiasByCapacidadId(dto.id())
                                            .collectList()
                                            .map(tecnologias ->
                                                    new CapacidadListDTO(
                                                            dto.id(),
                                                            dto.nombre(),
                                                            dto.descripcion(),
                                                            tecnologias
                                                    )
                                            )
                            );

                    return enrichedFlux.collectList()
                            .map(enrichedList ->
                                    new PageResult<>(
                                            enrichedList,
                                            page.getTotalElements(),
                                            page.getTotalPages(),
                                            page.getCurrentPage(),
                                            page.getPageSize(),
                                            page.isFirst(),
                                            page.isLast()
                                    )
                            );
                });
    }
}
