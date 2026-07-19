package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.utils.TecnologiaSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TecnologiaClientPort {
      Mono<Void> associateCapacidadWithTecnologias(Long capacidadId, List<Long> tecnologiasIds);
      Flux<TecnologiaSummary> findTecnologiasByCapacidadId(Long capacidadId);


}

