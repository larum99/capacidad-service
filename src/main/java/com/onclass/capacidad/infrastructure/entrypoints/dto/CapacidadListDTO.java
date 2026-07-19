package com.onclass.capacidad.infrastructure.entrypoints.dto;

import java.util.List;

public record CapacidadListDTO(
        Long id,
        String nombre,
        String descripcion,
        List<TecnologiaSummaryDTO> tecnologias
) {}
