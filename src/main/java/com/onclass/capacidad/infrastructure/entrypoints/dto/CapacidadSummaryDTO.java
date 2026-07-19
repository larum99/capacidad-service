package com.onclass.capacidad.infrastructure.entrypoints.dto;

import com.onclass.capacidad.domain.utils.TecnologiaSummary;

import java.util.List;

public record CapacidadSummaryDTO(
        Long id,
        String nombre,
        List<TecnologiaSummary> tecnologias
) {}
