package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadBootcampEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.QueryConstants;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CapacidadBootcampRepository extends ReactiveCrudRepository<CapacidadBootcampEntity, Long> {

    Flux<CapacidadBootcampEntity> findByCapacidadId(Long capacidadId);

    Mono<Long> countByCapacidadId(Long capacidadId);

    Flux<CapacidadBootcampEntity> findByBootcampId(Long bootcampId);

    Mono<Void> deleteByBootcampIdAndCapacidadId(Long bootcampId, Long capacidadId);

    @Query(QueryConstants.FIND_CAPACIDADES_BY_BOOTCAMP_ID)
    Flux<CapacidadEntity> findCapacidadesByBootcampId(Long bootcampId);

    @Query(QueryConstants.FIND_CAPACIDAD_IDS_BY_BOOTCAMP_ID)
    Flux<Long> findCapacidadIdsByBootcampId(Long bootcampId);

    @Modifying
    @Query(QueryConstants.DELETE_ALL_BY_BOOTCAMP_ID)
    Mono<Void> deleteAllByBootcampId(Long bootcampId);
}