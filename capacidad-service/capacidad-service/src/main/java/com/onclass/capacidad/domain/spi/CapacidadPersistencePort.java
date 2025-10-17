package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.utils.PageResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadPersistencePort {
    Mono<Capacidad> saveCapacidad(Capacidad capacidad);
    Mono<Boolean> existByNombre(String nombre);
    Mono<PageResult<Capacidad>> findAll(CapacidadCriteria criteria);
    Mono<Void> deleteByIds(List<Long> capacidadIds);
}
