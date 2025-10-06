package com.onclass.capacidad.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TecnologiaClientPort {
      Mono<Void> associateCapacidadWithTecnologias(Long capacidadId, List<Long> tecnologiasIds);
}

