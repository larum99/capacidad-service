package com.onclass.capacidad.domain.model;

import com.onclass.capacidad.domain.utils.TecnologiaSummary;

import java.util.List;

public record CapacidadConTecnologias(
        Long id,
        String nombre,
        String descripcion,
        List<TecnologiaSummary> tecnologias
) {}
