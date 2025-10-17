package com.onclass.capacidad.infrastructure.adapters.persistenceadapter;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.utils.PageResult;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadRepository;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .map(entity -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<PageResult<Capacidad>> findAll(CapacidadCriteria criteria) {
        return capacidadRepository.findAllByFilters(criteria)
                .map(capacidadEntityMapper::toModel)
                .collectList()
                .zipWith(capacidadRepository.countByFilters(criteria))
                .map(tuple -> {
                    List<Capacidad> content = tuple.getT1();
                    long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / criteria.getSize());
                    boolean isFirst = criteria.getPage() == 0;
                    boolean isLast = criteria.getPage() == totalPages - 1;

                    return new PageResult<>(
                            content,
                            totalElements,
                            totalPages,
                            criteria.getPage(),
                            criteria.getSize(),
                            isFirst,
                            isLast
                    );
                });
    }

    // ========== MÉTODO NUEVO IMPLEMENTADO ==========

    @Override
    public Mono<Void> deleteByIds(List<Long> capacidadIds) {
        // Delega la operación de borrado masivo directamente al repositorio
        return capacidadRepository.deleteByIds(capacidadIds);
    }
}