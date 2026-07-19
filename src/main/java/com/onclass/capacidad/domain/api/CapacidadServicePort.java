package com.onclass.capacidad.domain.api;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadConTecnologias;
import com.onclass.capacidad.domain.utils.PageResult;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadListDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapacidadServicePort {
    Mono<Capacidad> registrarCapacidad(Capacidad capacidad, String messageId);
    Mono<PageResult<CapacidadConTecnologias>> listarCapacidades(CapacidadCriteria criteria);
    Mono<Void> eliminarCapacidadesPorIds(List<Long> capacidadIds);
    Mono<Boolean> validateCapacidadesExist(List<Long> capacidadIds);
}
