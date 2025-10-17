package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadBootcampPersistencePort {
    Mono<CapacidadBootcamp> saveCapacidadBootcamp(CapacidadBootcamp relacion);
    Flux<Capacidad> findCapacidadesByBootcampId(Long bootcampId);
    Mono<List<CapacidadBootcamp>> findCapacidadesBootcampByBootcampId(Long bootcampId);
    Mono<Integer> countBootcampsByCapacidadId(Long capacidadId);
    Mono<List<Long>> deleteByBootcampId(Long bootcampId);
}