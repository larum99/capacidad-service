package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadSummaryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacidadBootcampPersistencePort {
    Mono<CapacidadBootcamp> saveCapacidadBootcamp(CapacidadBootcamp relacion);
    Flux<Capacidad> findCapacidadesByBootcampId(Long bootcampId);
}
