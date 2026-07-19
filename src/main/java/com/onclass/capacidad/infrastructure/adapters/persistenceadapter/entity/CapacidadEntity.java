package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import static com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.EntityConstants.TABLE_CAPACIDADES;

@Table(name = TABLE_CAPACIDADES)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CapacidadEntity {
    @Id
    private Long id;
    private String nombre;
    private String descripcion;
}
