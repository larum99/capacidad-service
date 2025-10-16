package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.utils.PageResult;
import reactor.core.publisher.Mono;

public interface CapacidadPersistencePort {
    Mono<Capacidad> saveCapacidad(Capacidad capacidad);
    Mono<Boolean> existByNombre(String nombre);
    Mono<PageResult<Capacidad>> findAll(CapacidadCriteria criteria);
}
