package com.onclass.capacidad.infrastructure.entrypoints.handler;

import com.onclass.capacidad.domain.api.CapacidadBootcampServicePort;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.exceptions.TechnicalException;
import com.onclass.capacidad.infrastructure.entrypoints.dto.BootcampCapacidadDTO;
import com.onclass.capacidad.infrastructure.entrypoints.mapper.CapacidadBootcampMapper;
import com.onclass.capacidad.infrastructure.entrypoints.util.APIResponse;
import com.onclass.capacidad.infrastructure.entrypoints.util.Constants;
import com.onclass.capacidad.infrastructure.entrypoints.util.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CapacidadBootcampHandlerImpl {

    private static final Logger log = LoggerFactory.getLogger(CapacidadBootcampHandlerImpl.class);

    private final CapacidadBootcampServicePort servicePort;
    private final CapacidadBootcampMapper mapper;

    public CapacidadBootcampHandlerImpl(CapacidadBootcampServicePort servicePort,
                                        CapacidadBootcampMapper mapper) {
        this.servicePort = servicePort;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> createCapacidadBootcamps(ServerRequest request) {
        String messageId = UUID.randomUUID().toString();

        return request.bodyToFlux(BootcampCapacidadDTO.class)
                .map(mapper::toModel)
                .collectList()
                .flatMapMany(list -> servicePort.registrarCapacidadBootcamps(list, messageId))
                .collectList()
                .flatMap(saved -> ServerResponse.status(HttpStatus.CREATED).bodyValue(saved));
    }

    public Mono<ServerResponse> listCapacidadesByBootcamp(ServerRequest request) {
        String messageId = getMessageId(request);
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));

        return servicePort
                .listarCapacidadesPorBootcamp(bootcampId)
                .collectList()
                .flatMap(list -> ServerResponse.ok().bodyValue(list))
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId))
                .doOnError(ex -> log.error("Error al listar capacidades para bootcamp {}", bootcampId, ex))
                .onErrorResume(ex -> buildErrorResponse(messageId, ex));
    }

    public Mono<ServerResponse> deleteCapacidadesByBootcamp(ServerRequest request) {
        String messageId = getMessageId(request);
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));

        return servicePort
                .eliminarCapacidadesPorBootcamp(bootcampId, messageId)
                .then(ServerResponse.noContent().build())
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId))
                .doOnSuccess(r -> log.info("Capacidades eliminadas para bootcamp {}", bootcampId))
                .doOnError(ex -> log.error("Error al eliminar capacidades del bootcamp {}", bootcampId, ex))
                .onErrorResume(ex -> buildErrorResponse(messageId, ex));
    }

    public Mono<ServerResponse> countBootcampsByCapacidadId(ServerRequest request) {
        String messageId = getMessageId(request);
        Long capacidadId = Long.valueOf(request.pathVariable("capacidadId"));

        return servicePort.countBootcampsByCapacidadId(capacidadId)
                .flatMap(count -> ServerResponse.ok().bodyValue(count))
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId))
                .doOnError(ex -> log.error("Error al contar bootcamps para capacidad {}: {}", capacidadId, ex.getMessage()))
                .onErrorResume(ex -> buildErrorResponse(messageId, ex));
    }

    private Mono<ServerResponse> buildErrorResponse(String messageId, Throwable ex) {
        if (ex instanceof BusinessException bex) {
            return buildError(HttpStatus.BAD_REQUEST, messageId, bex.getTechnicalMessage());
        }
        if (ex instanceof TechnicalException tex) {
            return buildError(HttpStatus.INTERNAL_SERVER_ERROR, messageId, tex.getTechnicalMessage());
        }
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, messageId, TechnicalMessage.INTERNAL_ERROR);
    }

    private Mono<ServerResponse> buildError(HttpStatus status, String identifier, TechnicalMessage error) {
        APIResponse apiErrorResponse = APIResponse.builder()
                .code(error.getCode())
                .message(error.getDescription())
                .identifier(identifier)
                .date(Instant.now().toString())
                .errors(List.of(ErrorDTO.builder()
                        .code(error.getCode())
                        .message(error.getDescription())
                        .param(error.getParam())
                        .build()))
                .build();
        return ServerResponse.status(status).bodyValue(apiErrorResponse);
    }

    private String getMessageId(ServerRequest serverRequest) {
        return Optional.ofNullable(serverRequest.headers().firstHeader(Constants.X_MESSAGE_ID))
                .orElse(UUID.randomUUID().toString());
    }
}

