package com.onclass.capacidad.application.configSwagger;

import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadBootcampHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@RouterOperation(
        path = ApiConstants.PATH_CAPACIDAD + "/{capacidadId}/bootcamps/count",
        beanClass = CapacidadBootcampHandlerImpl.class,
        beanMethod = "countBootcampsByCapacidadId",
        operation = @Operation(
                operationId = ApiConstants.COUNT_BOOTCAMPS_BY_CAPACIDAD_OPERATION_ID,
                summary = ApiConstants.COUNT_BOOTCAMPS_BY_CAPACIDAD_SUMMARY,
                description = "Retorna el número de bootcamps asociados a una capacidad específica.",
                parameters = {
                        @Parameter(
                                name = ApiConstants.HEADER_X_MESSAGE_ID,
                                in = ParameterIn.HEADER,
                                description = ApiConstants.HEADER_X_MESSAGE_ID_DESC,
                                required = true
                        ),
                        @Parameter(
                                name = "capacidadId",
                                in = ParameterIn.PATH,
                                description = "ID de la capacidad a consultar",
                                required = true
                        )
                },
                responses = {
                        @ApiResponse(responseCode = ApiConstants.HTTP_OK, description = ApiConstants.RESPONSE_200),
                        @ApiResponse(responseCode = ApiConstants.HTTP_NOT_FOUND, description = ApiConstants.RESPONSE_404),
                        @ApiResponse(responseCode = ApiConstants.HTTP_INTERNAL_ERROR, description = ApiConstants.RESPONSE_500)
                }
        )
)
public @interface CapacidadCountBootcampApiDoc {}
