package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper;

import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CapacidadEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "descripcion", source = "descripcion")
    Capacidad toModel(CapacidadEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "descripcion", source = "descripcion")
    CapacidadEntity toEntity(Capacidad capacidad);

    @Mapping(target = "tecnologias", ignore = true)
    CapacidadListDTO toListDTO(CapacidadEntity entity);

    List<CapacidadListDTO> toListDTOList(List<CapacidadEntity> entities);
}
