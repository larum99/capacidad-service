package com.onclass.capacidad.application.configSwagger;

import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@RouterOperation(
        path = ApiConstants.PATH_CAPACIDAD,
        beanClass = CapacidadHandlerImpl.class,
        beanMethod = ApiConstants.GET_CAPACIDADES_METHOD,
        operation = @Operation(
                operationId = ApiConstants.GET_CAPACIDADES_OPERATION_ID,
                summary = ApiConstants.GET_CAPACIDADES_SUMMARY,
                description = ApiConstants.GET_CAPACIDADES_DESCRIPTION,
                parameters = {
                        @Parameter(
                                name = ApiConstants.HEADER_X_MESSAGE_ID,
                                in = ParameterIn.HEADER,
                                description = ApiConstants.HEADER_X_MESSAGE_ID_DESC,
                                required = true
                        ),
                        @Parameter(
                                name = ApiConstants.PARAM_PAGE,
                                in = ParameterIn.QUERY,
                                description = ApiConstants.PARAM_PAGE_DESC
                        ),
                        @Parameter(
                                name = ApiConstants.PARAM_SIZE,
                                in = ParameterIn.QUERY,
                                description = ApiConstants.PARAM_SIZE_DESC
                        ),
                        @Parameter(
                                name = ApiConstants.PARAM_SORT_BY,
                                in = ParameterIn.QUERY,
                                description = ApiConstants.PARAM_SORT_BY_DESC
                        ),
                        @Parameter(
                                name = ApiConstants.PARAM_SORT_ORDER,
                                in = ParameterIn.QUERY,
                                description = ApiConstants.PARAM_SORT_ORDER_DESC
                        )
                },
                responses = {
                        @ApiResponse(
                                responseCode = ApiConstants.HTTP_OK,
                                description = ApiConstants.RESPONSE_200
                        ),
                        @ApiResponse(
                                responseCode = ApiConstants.HTTP_BAD_REQUEST,
                                description = ApiConstants.RESPONSE_400
                        ),
                        @ApiResponse(
                                responseCode = ApiConstants.HTTP_INTERNAL_ERROR,
                                description = ApiConstants.RESPONSE_500
                        )
                }
        )
)
public @interface CapacidadGetApiDoc {}
