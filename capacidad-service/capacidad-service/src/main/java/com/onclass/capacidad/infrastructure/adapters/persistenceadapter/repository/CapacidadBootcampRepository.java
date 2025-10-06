package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CapacidadBootcampRepository extends ReactiveCrudRepository<CapacidadBootcampEntity, Long> {

    Flux<CapacidadBootcampEntity> findByCapacidadId(Long capacidadId);

    Mono<Long> countByCapacidadId(Long capacidadId);
}
