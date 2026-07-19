package com.onclass.capacidad.infrastructure.adapters.persistenceadapter;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadBootcampEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadBootcampRepository;
import reactor.core.publisher.Mono;

public class CapacidadBootcampPersistenceAdapter implements CapacidadBootcampPersistencePort {

    private final CapacidadBootcampRepository repository;
    private final CapacidadBootcampEntityMapper mapper;

    public CapacidadBootcampPersistenceAdapter(CapacidadBootcampRepository repository,
                                               CapacidadBootcampEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<CapacidadBootcamp> saveCapacidadBootcamp(CapacidadBootcamp relacion) {
        return repository.save(mapper.toEntity(relacion))
                .map(mapper::toModel);
    }
}
