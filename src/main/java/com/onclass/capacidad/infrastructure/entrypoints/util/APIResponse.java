package com.onclass.capacidad.infrastructure.entrypoints.util;

import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class APIResponse {
    private String code;          // Código del mensaje técnico
    private String message;       // Mensaje técnico
    private String identifier;    // messageId
    private String date;          // Fecha/hora
    private CapacidadDTO data;    // Objeto de respuesta (cuando aplique)
    private List<ErrorDTO> errors; // Lista de errores
}
