package com.onclass.capacidad.infrastructure.entrypoints.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CapacidadDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String nombre;
    private String descripcion;
    private List<Long> tecnologias;
}
