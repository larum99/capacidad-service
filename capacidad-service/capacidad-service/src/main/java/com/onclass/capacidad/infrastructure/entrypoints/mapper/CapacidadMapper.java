package com.onclass.capacidad.infrastructure.entrypoints.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadDTO;
import com.onclass.capacidad.infrastructure.entrypoints.util.MapperConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapperConstants.COMPONENT_MODEL_SPRING)
public interface CapacidadMapper {

    @Mapping(target = MapperConstants.FIELD_ID, ignore = true)
    @Mapping(source = MapperConstants.FIELD_NOMBRE, target = MapperConstants.FIELD_NOMBRE)
    @Mapping(source = MapperConstants.FIELD_DESCRIPCION, target = MapperConstants.FIELD_DESCRIPCION)
    @Mapping(source = MapperConstants.FIELD_TECNOLOGIAS, target = MapperConstants.FIELD_TECNOLOGIAS)
    Capacidad toModel(CapacidadDTO dto);

    CapacidadDTO toDTO(Capacidad model);
}
