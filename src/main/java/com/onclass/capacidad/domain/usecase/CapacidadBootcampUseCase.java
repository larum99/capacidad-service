package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadBootcampServicePort;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

public class CapacidadBootcampUseCase implements CapacidadBootcampServicePort {

    private final CapacidadBootcampPersistencePort persistencePort;

    public CapacidadBootcampUseCase(CapacidadBootcampPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Flux<CapacidadBootcamp> registrarCapacidadBootcamps(List<CapacidadBootcamp> relaciones, String messageId) {
        return Flux.fromIterable(relaciones)
                .flatMap(persistencePort::saveCapacidadBootcamp);
    }
}