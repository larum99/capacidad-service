package com.onclass.capacidad.infrastructure.adapters.persistenceadapter;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadRepository;
import reactor.core.publisher.Mono;

public class CapacidadPersistenceAdapter implements CapacidadPersistencePort {

    private final CapacidadRepository capacidadRepository;
    private final CapacidadEntityMapper capacidadEntityMapper;

    public CapacidadPersistenceAdapter(CapacidadRepository capacidadRepository,
                                       CapacidadEntityMapper capacidadEntityMapper) {
        this.capacidadRepository = capacidadRepository;
        this.capacidadEntityMapper = capacidadEntityMapper;
    }

    @Override
    public Mono<Capacidad> saveCapacidad(Capacidad capacidad) {
        return capacidadRepository.save(capacidadEntityMapper.toEntity(capacidad))
                .map(capacidadEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existByNombre(String nombre) {
        return capacidadRepository.findByNombre(nombre)
                .map(capacidadEntityMapper::toModel)
                .map(c -> true)
                .defaultIfEmpty(false);
    }
}
