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
        path = ApiConstants.PATH_CAPACIDAD_BOOTCAMP + ApiConstants.PATH_BOOTCAMP_ID + ApiConstants.PATH_CAPACIDADES,
        beanClass = CapacidadBootcampHandlerImpl.class,
        beanMethod = ApiConstants.LIST_CAPACIDADES_BY_BOOTCAMP_METHOD,
        operation = @Operation(
                operationId = ApiConstants.GET_CAPACIDADES_BY_BOOTCAMP_OPERATION_ID,
                summary = ApiConstants.GET_CAPACIDADES_BY_BOOTCAMP_SUMMARY,
                description = ApiConstants.GET_CAPACIDADES_BY_BOOTCAMP_DESCRIPTION,
                parameters = {
                        @Parameter(
                                name = ApiConstants.HEADER_X_MESSAGE_ID,
                                in = ParameterIn.HEADER,
                                description = ApiConstants.HEADER_X_MESSAGE_ID_DESC,
                                required = true
                        ),
                        @Parameter(
                                name = ApiConstants.PARAM_BOOTCAMP_ID,
                                in = ParameterIn.PATH,
                                description = ApiConstants.PARAM_BOOTCAMP_ID_DESC_SIMPLE,
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
