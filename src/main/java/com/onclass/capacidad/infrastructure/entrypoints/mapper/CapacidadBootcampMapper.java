package com.onclass.capacidad.infrastructure.entrypoints.mapper;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadBootcampDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacidadBootcampMapper {
    @Mapping(target = "id", ignore = true)
    CapacidadBootcamp toModel(CapacidadBootcampDTO dto);

    CapacidadBootcampDTO toDTO(CapacidadBootcamp model);
}
