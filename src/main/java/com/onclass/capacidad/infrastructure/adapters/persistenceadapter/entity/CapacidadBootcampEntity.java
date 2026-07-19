package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "capacidad_bootcamp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapacidadBootcampEntity {

    @Id
    private Long id;

    @Column("id_capacidad")
    private Long capacidadId;

    @Column("id_bootcamp")
    private Long bootcampId;
}

