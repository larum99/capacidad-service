package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadBootcampServicePort;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.CapacidadConTecnologias;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.TecnologiaSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

public class CapacidadBootcampUseCase implements CapacidadBootcampServicePort {

    private final CapacidadBootcampPersistencePort persistencePort;
    private final TecnologiaClientPort tecnologiaClientPort;
    private final CapacidadPersistencePort capacidadPersistencePort;

    public CapacidadBootcampUseCase(CapacidadBootcampPersistencePort persistencePort,
                                    CapacidadPersistencePort capacidadPersistencePort,
                                    TecnologiaClientPort tecnologiaClientPort) {
        this.persistencePort = persistencePort;
        this.capacidadPersistencePort = capacidadPersistencePort;
        this.tecnologiaClientPort = tecnologiaClientPort;
    }

    @Override
    public Flux<CapacidadBootcamp> registrarCapacidadBootcamps(List<CapacidadBootcamp> relaciones, String messageId) {
        return Flux.fromIterable(relaciones)
                .flatMap(persistencePort::saveCapacidadBootcamp);
    }

    @Override
    public Flux<CapacidadConTecnologias> listarCapacidadesPorBootcamp(Long bootcampId) {
        return persistencePort.findCapacidadesByBootcampId(bootcampId)
                .concatMap(capacidad ->
                        tecnologiaClientPort.findTecnologiasByCapacidadId(capacidad.id())
                                .map(t -> new TecnologiaSummary(t.getId(), t.getNombre()))
                                .collectList()
                                .map(tecnologias -> new CapacidadConTecnologias(
                                        capacidad.id(),
                                        capacidad.nombre(),
                                        capacidad.descripcion(),
                                        tecnologias
                                ))
                );
    }

    @Override
    public Mono<List<Long>> eliminarCapacidadesPorBootcamp(Long bootcampId, String messageId) {
        return persistencePort.deleteByBootcampId(bootcampId);
    }

    @Override
    public Mono<Integer> countBootcampsByCapacidadId(Long capacidadId) {
        return persistencePort.countBootcampsByCapacidadId(capacidadId);
    }
}