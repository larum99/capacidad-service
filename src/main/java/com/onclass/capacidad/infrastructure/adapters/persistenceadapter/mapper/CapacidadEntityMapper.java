package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.MapperConstants;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = MapperConstants.COMPONENT_MODEL_SPRING)
public interface CapacidadEntityMapper {

    @Mapping(target = MapperConstants.FIELD_ID, source = MapperConstants.FIELD_ID)
    @Mapping(target = MapperConstants.FIELD_NOMBRE, source = MapperConstants.FIELD_NOMBRE)
    @Mapping(target = MapperConstants.FIELD_DESCRIPCION, source = MapperConstants.FIELD_DESCRIPCION)
    Capacidad toModel(CapacidadEntity entity);

    @Mapping(target = MapperConstants.FIELD_ID, source = MapperConstants.FIELD_ID)
    @Mapping(target = MapperConstants.FIELD_NOMBRE, source = MapperConstants.FIELD_NOMBRE)
    @Mapping(target = MapperConstants.FIELD_DESCRIPCION, source = MapperConstants.FIELD_DESCRIPCION)
    CapacidadEntity toEntity(Capacidad capacidad);

    @Mapping(target = MapperConstants.FIELD_TECNOLOGIAS, ignore = true)
    CapacidadListDTO toListDTO(CapacidadEntity entity);

    List<CapacidadListDTO> toListDTOList(List<CapacidadEntity> entities);
}
