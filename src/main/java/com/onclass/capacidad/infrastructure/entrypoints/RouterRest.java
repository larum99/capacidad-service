package com.onclass.capacidad.infrastructure.entrypoints;

import com.onclass.capacidad.application.configSwagger.*;
import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadBootcampHandlerImpl;
import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadHandlerImpl;
import com.onclass.capacidad.infrastructure.entrypoints.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @CapacidadApiDoc
    public RouterFunction<ServerResponse> createCapacidadRoute(CapacidadHandlerImpl handler) {
        return route(POST(Constants.CAPACIDAD_PATH), handler::createCapacidad);
    }

    @Bean
    @CapacidadGetApiDoc
    public RouterFunction<ServerResponse> getCapacidadesRoute(CapacidadHandlerImpl handler) {
        return route(GET(Constants.CAPACIDAD_PATH), handler::getCapacidades);
    }


    @Bean
    @CapacidadBootcampApiDoc
    public RouterFunction<ServerResponse> capacidadBootcampRoutes(CapacidadBootcampHandlerImpl handler) {
        return route()
                .POST(Constants.CAPACIDAD_BOOTCAMP_PATH, handler::createCapacidadBootcamps)
                .build();
    }

    @Bean
    @CapacidadBootcampGetApiDoc
    public RouterFunction<ServerResponse> getCapacidadesByBootcamp(CapacidadBootcampHandlerImpl handler) {
        return route(GET(ApiConstants.PATH_CAPACIDAD_BOOTCAMP + "/{bootcampId}/capacidades"),
                handler::listCapacidadesByBootcamp);
    }

    @Bean
    @CapacidadBootcampDeleteApiDoc
    public RouterFunction<ServerResponse> deleteCapacidadesByBootcamp(CapacidadBootcampHandlerImpl handler) {
        return route(DELETE(ApiConstants.PATH_CAPACIDAD_BOOTCAMP + "/{bootcampId}"),
                handler::deleteCapacidadesByBootcamp);
    }

    @Bean
    @CapacidadCountBootcampApiDoc
    public RouterFunction<ServerResponse> countBootcampsByCapacidadRoute(CapacidadBootcampHandlerImpl handler) {
        return route(GET(Constants.CAPACIDAD_PATH + "/{capacidadId}/bootcamps/count"),
                handler::countBootcampsByCapacidadId);
    }

    @Bean
    @CapacidadDeleteByIdsApiDoc
    public RouterFunction<ServerResponse> deleteCapacidadesByIdsRoute(CapacidadHandlerImpl handler) {
        return route(DELETE(Constants.CAPACIDAD_PATH),
                handler::deleteCapacidadesByIds);
    }
}
