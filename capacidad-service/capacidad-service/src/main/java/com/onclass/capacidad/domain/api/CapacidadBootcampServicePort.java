package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.CapacidadConTecnologias;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadBootcampServicePort {
    Flux<CapacidadBootcamp> registrarCapacidadBootcamps(List<CapacidadBootcamp> relaciones, String messageId);
    Flux<CapacidadConTecnologias> listarCapacidadesPorBootcamp(Long bootcampId);

    Mono<List<Long>> eliminarCapacidadesPorBootcamp(Long bootcampId, String messageId);
    Mono<Integer> countBootcampsByCapacidadId(Long capacidadId);

}

