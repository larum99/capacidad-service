package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadServicePort;
import com.onclass.capacidad.domain.constants.Constants;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
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
}
