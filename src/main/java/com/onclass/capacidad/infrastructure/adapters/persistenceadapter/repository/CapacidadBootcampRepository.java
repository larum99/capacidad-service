package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadBootcampEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
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

    @Query("""
    SELECT c.id AS id, c.nombre AS nombre, c.descripcion AS descripcion
    FROM capacidad_bootcamp cb
    JOIN capacidades c ON cb.id_capacidad = c.id
    WHERE cb.id_bootcamp = :bootcampId
    """)
    Flux<CapacidadEntity> findCapacidadesByBootcampId(Long bootcampId);

    @Query("SELECT id_capacidad FROM capacidad_bootcamp WHERE id_bootcamp = :bootcampId")
    Flux<Long> findCapacidadIdsByBootcampId(Long bootcampId);

    @Modifying
    @Query("DELETE FROM capacidad_bootcamp WHERE id_bootcamp = :bootcampId")
    Mono<Void> deleteAllByBootcampId(Long bootcampId);
}