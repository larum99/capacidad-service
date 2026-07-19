package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomCapacidadRepository {
    Flux<CapacidadEntity> findAllByFilters(CapacidadCriteria criteria);
    Mono<Long> countByFilters(CapacidadCriteria criteria);
}
