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
        path = ApiConstants.PATH_CAPACIDAD_BOOTCAMP + "/{bootcampId}/capacidades",
        beanClass = CapacidadBootcampHandlerImpl.class,
        beanMethod = "listCapacidadesByBootcamp",
        operation = @Operation(
                operationId = "getCapacidadesByBootcamp",
                summary = "Obtiene todas las capacidades asociadas a un bootcamp",
                description = "Retorna todas las capacidades que están asociadas a un bootcamp específico según su ID.",
                parameters = {
                        @Parameter(
                                name = ApiConstants.HEADER_X_MESSAGE_ID,
                                in = ParameterIn.HEADER,
                                description = ApiConstants.HEADER_X_MESSAGE_ID_DESC,
                                required = true
                        ),
                        @Parameter(
                                name = "bootcampId",
                                in = ParameterIn.PATH,
                                description = "ID del bootcamp",
                                required = true
                        )
                },
                responses = {
                        @ApiResponse(responseCode = ApiConstants.HTTP_OK, description = ApiConstants.RESPONSE_200),
                        @ApiResponse(responseCode = ApiConstants.HTTP_BAD_REQUEST, description = ApiConstants.RESPONSE_400),
                        @ApiResponse(responseCode = ApiConstants.HTTP_INTERNAL_ERROR, description = ApiConstants.RESPONSE_500)
                }
        )
)
public @interface CapacidadBootcampGetApiDoc {}
