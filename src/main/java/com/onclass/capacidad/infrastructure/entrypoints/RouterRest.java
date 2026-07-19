package com.onclass.capacidad.infrastructure.entrypoints;

import com.onclass.capacidad.application.configSwagger.CapacidadApiDoc;
import com.onclass.capacidad.application.configSwagger.CapacidadBootcampApiDoc;
import com.onclass.capacidad.application.configSwagger.CapacidadGetApiDoc;
import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadBootcampHandlerImpl;
import com.onclass.capacidad.infrastructure.entrypoints.handler.CapacidadHandlerImpl;
import com.onclass.capacidad.infrastructure.entrypoints.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
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
}
