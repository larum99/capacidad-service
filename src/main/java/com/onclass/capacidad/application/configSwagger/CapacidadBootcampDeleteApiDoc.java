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
        path = ApiConstants.PATH_CAPACIDAD_BOOTCAMP + "/{bootcampId}",
        beanClass = CapacidadBootcampHandlerImpl.class,
        beanMethod = "deleteCapacidadesByBootcamp",
        operation = @Operation(
                operationId = ApiConstants.DELETE_CAPACIDAD_BOOTCAMP_OPERATION_ID,
                summary = ApiConstants.DELETE_CAPACIDAD_BOOTCAMP_SUMMARY,
                description = "Elimina las capacidades asociadas a un bootcamp específico. " +
                        "La operación es transaccional e ignora las capacidades compartidas con otros bootcamps.",
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
                                description = "ID del bootcamp cuyas capacidades serán eliminadas",
                                required = true
                        )
                },
                responses = {
                        @ApiResponse(responseCode = ApiConstants.HTTP_NO_CONTENT, description = ApiConstants.RESPONSE_204),
                        @ApiResponse(responseCode = ApiConstants.HTTP_NOT_FOUND, description = ApiConstants.RESPONSE_404),
                        @ApiResponse(responseCode = ApiConstants.HTTP_INTERNAL_ERROR, description = ApiConstants.RESPONSE_500)
                }
        )
)
public @interface CapacidadBootcampDeleteApiDoc {}
