package com.onclass.capacidad.domain.model;

import com.onclass.capacidad.domain.utils.TecnologiaSummary;

import java.util.List;

public record CapacidadSummary(
        Long id,
        String nombre,
        List<TecnologiaSummary> tecnologias
) {}
