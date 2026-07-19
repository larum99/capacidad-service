package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadBootcampEntity;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadSummaryDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CapacidadBootcampRepository extends ReactiveCrudRepository<CapacidadBootcampEntity, Long> {

    Flux<CapacidadBootcampEntity> findByCapacidadId(Long capacidadId);

    Mono<Long> countByCapacidadId(Long capacidadId);

    @Query("""
        SELECT c.id AS id, c.nombre AS nombre
        FROM capacidad_bootcamp cb
        JOIN capacidades c ON cb.id_capacidad = c.id
        WHERE cb.id_bootcamp = :bootcampId
    """)
    Flux<CapacidadSummaryDTO> findCapacidadesByBootcampId(Long bootcampId);
}
