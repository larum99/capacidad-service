package com.onclass.capacidad.infrastructure.adapters.persistenceadapter;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.utils.PageResult;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadRepository;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadListDTO;
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

    @Override
    public Mono<PageResult<CapacidadListDTO>> findAll(CapacidadCriteria criteria) {
        return capacidadRepository.findAllByFilters(criteria)
                .map(capacidadEntityMapper::toListDTO)
                .collectList()
                .zipWith(capacidadRepository.countByFilters(criteria))
                .map(tuple -> {
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / criteria.getSize());
                    boolean isFirst = criteria.getPage() == 0;
                    boolean isLast = criteria.getPage() == totalPages - 1;

                    return new PageResult<>(
                            tuple.getT1(),
                            totalElements,
                            totalPages,
                            criteria.getPage(),
                            criteria.getSize(),
                            isFirst,
                            isLast
                    );
                });
    }
}
