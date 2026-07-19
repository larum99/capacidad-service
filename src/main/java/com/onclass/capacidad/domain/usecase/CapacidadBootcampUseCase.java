package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadBootcampServicePort;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.CapacidadConTecnologias;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.TecnologiaSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapacidadBootcampUseCase implements CapacidadBootcampServicePort {

    private final CapacidadBootcampPersistencePort persistencePort;
    private final TecnologiaClientPort tecnologiaClientPort;

    public CapacidadBootcampUseCase(CapacidadBootcampPersistencePort persistencePort,
                                    TecnologiaClientPort tecnologiaClientPort) {
        this.persistencePort = persistencePort;
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
                .concatMap(this::enriquecerCapacidadConTecnologias);
    }

    private Mono<CapacidadConTecnologias> enriquecerCapacidadConTecnologias(com.onclass.capacidad.domain.model.Capacidad capacidad) {
        return obtenerTecnologiasDeCapacidad(capacidad.id())
                .map(tecnologias -> crearCapacidadConTecnologias(capacidad, tecnologias));
    }

    private Mono<List<TecnologiaSummary>> obtenerTecnologiasDeCapacidad(Long capacidadId) {
        return tecnologiaClientPort.findTecnologiasByCapacidadId(capacidadId)
                .map(this::convertirATecnologiaSummary)
                .collectList();
    }

    private TecnologiaSummary convertirATecnologiaSummary(com.onclass.capacidad.domain.utils.TecnologiaSummary tecnologia) {
        return new TecnologiaSummary(tecnologia.getId(), tecnologia.getNombre());
    }

    private CapacidadConTecnologias crearCapacidadConTecnologias(com.onclass.capacidad.domain.model.Capacidad capacidad, List<TecnologiaSummary> tecnologias) {
        return new CapacidadConTecnologias(
                capacidad.id(),
                capacidad.nombre(),
                capacidad.descripcion(),
                tecnologias
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