package com.onclass.capacidad.domain.spi;

import com.onclass.capacidad.domain.model.Capacidad;
import reactor.core.publisher.Mono;

public interface CapacidadPersistencePort {
    Mono<Capacidad> saveCapacidad(Capacidad capacidad);
    Mono<Boolean> existByNombre(String nombre);
}
