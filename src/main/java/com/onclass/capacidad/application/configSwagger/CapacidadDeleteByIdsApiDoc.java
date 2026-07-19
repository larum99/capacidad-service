package com.onclass.capacidad.application.configSwagger;

import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@RouterOperation(
        path = ApiConstants.PATH_CAPACIDAD,
        beanClass = CapacidadHandlerImpl.class,
        beanMethod = "deleteCapacidadesByIds",
        operation = @Operation(
                operationId = ApiConstants.DELETE_CAPACIDADES_BY_IDS_OPERATION_ID,
                summary = ApiConstants.DELETE_CAPACIDADES_BY_IDS_SUMMARY,
                description = "Elimina varias capacidades según una lista de IDs enviados en el cuerpo de la solicitud.",
                parameters = {
                        @Parameter(
                                name = ApiConstants.HEADER_X_MESSAGE_ID,
                                in = ParameterIn.HEADER,
                                description = ApiConstants.HEADER_X_MESSAGE_ID_DESC,
                                required = true
                        )
                },
                requestBody = @RequestBody(
                        required = true,
                        description = "Lista de IDs de capacidades a eliminar",
                        content = @Content(
                                schema = @Schema(implementation = List.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Ejemplo eliminación por IDs",
                                                value = "[1, 2, 3]"
                                        )
                                }
                        )
                ),
                responses = {
                        @ApiResponse(responseCode = ApiConstants.HTTP_NO_CONTENT, description = ApiConstants.RESPONSE_204),
                        @ApiResponse(responseCode = ApiConstants.HTTP_BAD_REQUEST, description = ApiConstants.RESPONSE_400),
                        @ApiResponse(responseCode = ApiConstants.HTTP_INTERNAL_ERROR, description = ApiConstants.RESPONSE_500)
                }
        )
)
public @interface CapacidadDeleteByIdsApiDoc {}
