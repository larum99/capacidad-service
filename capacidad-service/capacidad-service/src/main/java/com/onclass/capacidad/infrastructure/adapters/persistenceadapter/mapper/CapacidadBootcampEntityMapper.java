package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadBootcampEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.MapperConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapperConstants.COMPONENT_MODEL_SPRING)
public interface CapacidadBootcampEntityMapper {
    @Mapping(source = MapperConstants.FIELD_CAPACIDAD_ID, target = MapperConstants.FIELD_CAPACIDAD_ID)
    @Mapping(source = MapperConstants.FIELD_BOOTCAMP_ID, target = MapperConstants.FIELD_BOOTCAMP_ID)
    CapacidadBootcamp toModel(CapacidadBootcampEntity entity);

    @Mapping(source = MapperConstants.FIELD_CAPACIDAD_ID, target = MapperConstants.FIELD_CAPACIDAD_ID)
    @Mapping(source = MapperConstants.FIELD_BOOTCAMP_ID, target = MapperConstants.FIELD_BOOTCAMP_ID)
    CapacidadBootcampEntity toEntity(CapacidadBootcamp model);
}
