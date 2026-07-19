package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.QueryConstants;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface CapacidadRepository extends ReactiveCrudRepository<CapacidadEntity, Long>, CustomCapacidadRepository {

    Mono<CapacidadEntity> findByNombre(String nombre);

    @Modifying
    @Query(QueryConstants.DELETE_BY_IDS)
    Mono<Void> deleteByIds(List<Long> capacidadIds);

    @Query(QueryConstants.COUNT_BY_ID_IN)
    Mono<Long> countByIdIn(List<Long> capacidadIds);
}