package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CapacidadRepository extends ReactiveCrudRepository<CapacidadEntity, Long>, CustomCapacidadRepository {

    Mono<CapacidadEntity> findByNombre(String nombre);

}
