package com.onclass.capacidad.infrastructure.entrypoints.mapper;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.infrastructure.entrypoints.dto.BootcampCapacidadDTO;
import com.onclass.capacidad.infrastructure.entrypoints.util.MapperConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapperConstants.COMPONENT_MODEL_SPRING)
public interface CapacidadBootcampMapper {
    @Mapping(target = MapperConstants.FIELD_ID, ignore = true)
    CapacidadBootcamp toModel(BootcampCapacidadDTO dto);

    BootcampCapacidadDTO toDTO(CapacidadBootcamp model);
}
