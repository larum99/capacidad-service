package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadBootcampServicePort;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.CapacidadSummary;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadSummaryDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
    public Flux<CapacidadSummary> listarCapacidadesPorBootcamp(Long bootcampId) {
        return persistencePort.findCapacidadesByBootcampId(bootcampId)
                .flatMap(capacidad ->
                        tecnologiaClientPort.findTecnologiasByCapacidadId(capacidad.id())
                                .collectList()
                                .map(tecnologias ->
                                        new CapacidadSummary(
                                                capacidad.id(),
                                                capacidad.nombre(),
                                                tecnologias
                                        )
                                )
                );
    }

}