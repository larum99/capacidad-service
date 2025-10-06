package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import reactor.core.publisher.Mono;

public interface CapacidadBootcampPersistencePort {
    Mono<CapacidadBootcamp> saveCapacidadBootcamp(CapacidadBootcamp relacion);
}
