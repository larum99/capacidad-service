package com.onclass.capacidad.infrastructure.entrypoints.handler;

import com.onclass.capacidad.domain.api.CapacidadServicePort;
import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.exceptions.TechnicalException;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadDTO;
import com.onclass.capacidad.infrastructure.entrypoints.mapper.CapacidadMapper;
import com.onclass.capacidad.infrastructure.entrypoints.util.APIResponse;
import com.onclass.capacidad.infrastructure.entrypoints.util.Constants;
import com.onclass.capacidad.infrastructure.entrypoints.util.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
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
public class CapacidadHandlerImpl {

    private static final Logger log = LoggerFactory.getLogger(CapacidadHandlerImpl.class);

    private final CapacidadServicePort capacidadServicePort;
    private final CapacidadMapper capacidadMapper;

    public CapacidadHandlerImpl(CapacidadServicePort capacidadServicePort,
                                CapacidadMapper capacidadMapper) {
        this.capacidadServicePort = capacidadServicePort;
        this.capacidadMapper = capacidadMapper;
    }

    public Mono<ServerResponse> createCapacidad(ServerRequest request) {
        String messageId = getMessageId(request);

        return request.bodyToMono(CapacidadDTO.class)
                .flatMap(dto -> capacidadServicePort
                        .registrarCapacidad(capacidadMapper.toModel(dto), messageId)
                        .doOnSuccess(saved -> log.info("Capacidad creada con messageId: {}", messageId)))
                .flatMap(saved -> {
                    APIResponse successResponse = APIResponse.builder()
                            .code(TechnicalMessage.CAPACIDAD_CREATED.getCode())
                            .message(TechnicalMessage.CAPACIDAD_CREATED.getDescription())
                            .identifier(messageId)
                            .date(Instant.now().toString())
                            .data(capacidadMapper.toDTO(saved))
                            .build();
                    return ServerResponse
                            .status(HttpStatus.CREATED)
                            .bodyValue(successResponse);
                })
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId))
                .doOnError(ex -> log.error(Constants.CAPACIDAD_ERROR, ex))
                .onErrorResume(ex -> handleErrors(ex, messageId));
    }

    public Mono<ServerResponse> getCapacidades(ServerRequest request) {
        String messageId = getMessageId(request);

        int page = parseQueryParam(request, "page", 0);
        int size = parseQueryParam(request, "size", 10);
        String sortBy = request.queryParam("sortBy").orElse("nombre");

        String sortOrder = request.queryParam("sortOrder").orElse("asc");

        CapacidadCriteria criteria = new CapacidadCriteria();
        criteria.setPage(page);
        criteria.setSize(size);
        criteria.setSortBy(sortBy);
        criteria.setSortOrder(sortOrder);

        return capacidadServicePort.listarCapacidades(criteria)
                .flatMap(pageResult -> ServerResponse.ok().bodyValue(pageResult))
                .onErrorResume(ex -> handleErrors(ex, messageId))
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId));
    }

    public Mono<ServerResponse> deleteCapacidadesByIds(ServerRequest request) {
        String messageId = getMessageId(request);

        return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .flatMap(capacidadIds -> {
                    log.info("Petición para eliminar capacidades con IDs: {}", capacidadIds);
                    return capacidadServicePort.eliminarCapacidadesPorIds(capacidadIds)
                            .then(ServerResponse.noContent().build());
                })
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId))
                .onErrorResume(ex -> handleErrors(ex, messageId));
    }

    public Mono<ServerResponse> validateCapacidadesExist(ServerRequest request) {
        String messageId = getMessageId(request);

        return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .flatMap(capacidadIds -> 
                    capacidadServicePort.validateCapacidadesExist(capacidadIds)
                            .flatMap(allExist -> ServerResponse.ok().bodyValue(allExist))
                )
                .contextWrite(Context.of(Constants.X_MESSAGE_ID, messageId))
                .onErrorResume(ex -> handleErrors(ex, messageId));
    }

    private Mono<ServerResponse> handleErrors(Throwable ex, String messageId) {
        log.error("Error procesando solicitud con messageId: {}", messageId, ex);

        if (ex instanceof BusinessException businessEx) {
            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    messageId,
                    businessEx.getTechnicalMessage(),
                    List.of(ErrorDTO.builder()
                            .code(businessEx.getTechnicalMessage().getCode())
                            .message(businessEx.getTechnicalMessage().getDescription())
                            .param(businessEx.getTechnicalMessage().getParam())
                            .build()));
        }

        if (ex instanceof TechnicalException techEx) {
            return buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageId,
                    techEx.getTechnicalMessage(),
                    List.of(ErrorDTO.builder()
                            .code(techEx.getTechnicalMessage().getCode())
                            .message(techEx.getTechnicalMessage().getDescription())
                            .param(techEx.getTechnicalMessage().getParam())
                            .build()));
        }

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                messageId,
                TechnicalMessage.INTERNAL_ERROR,
                List.of(ErrorDTO.builder()
                        .code(TechnicalMessage.INTERNAL_ERROR.getCode())
                        .message(TechnicalMessage.INTERNAL_ERROR.getDescription())
                        .build()));
    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, String identifier,
                                                    TechnicalMessage error, List<ErrorDTO> errors) {
        APIResponse apiErrorResponse = APIResponse.builder()
                .code(error.getCode())
                .message(error.getDescription())
                .identifier(identifier)
                .date(Instant.now().toString())
                .errors(errors)
                .build();
        return ServerResponse.status(httpStatus).bodyValue(apiErrorResponse);
    }

    private String getMessageId(ServerRequest serverRequest) {
        return Optional.ofNullable(serverRequest.headers().firstHeader(Constants.X_MESSAGE_ID))
                .orElse(UUID.randomUUID().toString());
    }

    private int parseQueryParam(ServerRequest request, String name, int defaultValue) {
        return request.queryParam(name)
                .map(Integer::parseInt)
                .orElse(defaultValue);
    }
}
