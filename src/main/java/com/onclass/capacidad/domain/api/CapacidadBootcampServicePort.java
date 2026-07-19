package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadBootcampServicePort {
    Flux<CapacidadBootcamp> registrarCapacidadBootcamps(List<CapacidadBootcamp> relaciones, String messageId);
}

