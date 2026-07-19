package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacidadBootcampEntityMapper {
    @Mapping(source = "capacidadId", target = "capacidadId")
    @Mapping(source = "bootcampId", target = "bootcampId")
    CapacidadBootcamp toModel(CapacidadBootcampEntity entity);

    @Mapping(source = "capacidadId", target = "capacidadId")
    @Mapping(source = "bootcampId", target = "bootcampId")
    CapacidadBootcampEntity toEntity(CapacidadBootcamp model);
}
