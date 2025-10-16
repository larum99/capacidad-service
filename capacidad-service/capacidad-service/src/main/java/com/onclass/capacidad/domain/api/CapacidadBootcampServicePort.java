package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.CapacidadConTecnologias;
import reactor.core.publisher.Flux;

import java.util.List;

public interface CapacidadBootcampServicePort {
    Flux<CapacidadBootcamp> registrarCapacidadBootcamps(List<CapacidadBootcamp> relaciones, String messageId);
    Flux<CapacidadConTecnologias> listarCapacidadesPorBootcamp(Long bootcampId);
}

