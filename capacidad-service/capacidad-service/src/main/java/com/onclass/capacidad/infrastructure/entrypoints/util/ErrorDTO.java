package com.onclass.capacidad.infrastructure.entrypoints.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    private String code;   // Código del error
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String param;  // Campo asociado al error
    private String message; // Mensaje legible
}
