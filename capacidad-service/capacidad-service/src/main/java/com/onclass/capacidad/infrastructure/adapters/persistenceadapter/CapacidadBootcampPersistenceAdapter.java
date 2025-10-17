package com.onclass.capacidad.infrastructure.adapters.persistenceadapter;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadBootcampEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadBootcampRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapacidadBootcampPersistenceAdapter implements CapacidadBootcampPersistencePort {

    private final CapacidadBootcampRepository repository;
    private final CapacidadBootcampEntityMapper mapper;

    public CapacidadBootcampPersistenceAdapter(CapacidadBootcampRepository repository,
                                               CapacidadBootcampEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // ... (los otros métodos como save, find, etc., permanecen igual) ...

    @Override
    public Mono<CapacidadBootcamp> saveCapacidadBootcamp(CapacidadBootcamp relacion) {
        return repository.save(mapper.toEntity(relacion))
                .map(mapper::toModel);
    }

    @Override
    public Flux<Capacidad> findCapacidadesByBootcampId(Long bootcampId) {
        return repository.findCapacidadesByBootcampId(bootcampId)
                .map(entity -> new Capacidad(
                        entity.getId(),
                        entity.getNombre(),
                        entity.getDescripcion(),
                        null
                ));
    }

    @Override
    public Mono<List<CapacidadBootcamp>> findCapacidadesBootcampByBootcampId(Long bootcampId) {
        return repository.findByBootcampId(bootcampId)
                .map(mapper::toModel)
                .collectList();
    }

    @Override
    public Mono<Integer> countBootcampsByCapacidadId(Long capacidadId) {
        return repository.countByCapacidadId(capacidadId)
                .map(Long::intValue);
    }

    /**
     * Implementación en dos pasos para compatibilidad con MySQL.
     */
    @Override
    public Mono<List<Long>> deleteByBootcampId(Long bootcampId) {
        // PASO 1: Obtener la lista de IDs de capacidad que se van a eliminar.
        return repository.findCapacidadIdsByBootcampId(bootcampId)
                .collectList()
                .flatMap(capacidadIds -> {
                    if (capacidadIds.isEmpty()) {
                        return Mono.just(capacidadIds); // No hay nada que borrar, devuelve la lista vacía.
                    }
                    // PASO 2: Ejecutar el borrado y, cuando termine, devolver la lista de IDs que obtuvimos en el paso 1.
                    return repository.deleteAllByBootcampId(bootcampId)
                            .thenReturn(capacidadIds);
                });
    }
}