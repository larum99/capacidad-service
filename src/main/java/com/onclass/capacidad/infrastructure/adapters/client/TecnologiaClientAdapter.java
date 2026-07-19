package com.onclass.capacidad.infrastructure.adapters.client;

import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.TecnologiaSummary;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadTecnologiaDTO;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaSummaryDTO;
import com.onclass.capacidad.infrastructure.entrypoints.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class TecnologiaClientAdapter implements TecnologiaClientPort {

    private final WebClient webClient;

    public TecnologiaClientAdapter(WebClient.Builder webClientBuilder,
                                   @Value("${services.tecnologia.url}") String tecnologiaUrl) {
        this.webClient = webClientBuilder
                .baseUrl(tecnologiaUrl)
                .build();
    }

    @Override
    public Mono<Void> associateCapacidadWithTecnologias(Long capacidadId, List<Long> tecnologiasIds) {
        return Flux.fromIterable(tecnologiasIds)
                .map(techId -> new CapacidadTecnologiaDTO(capacidadId, techId))
                .collectList()
                .flatMap(dtos ->
                        webClient.post()
                                .uri("/capacidad-tecnologias")
                                .header(Constants.X_MESSAGE_ID, "12345")
                                .bodyValue(dtos)
                                .retrieve()
                                .bodyToMono(Void.class)
                );
    }

    @Override
    public Flux<TecnologiaSummary> findTecnologiasByCapacidadId(Long capacidadId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/capacidad-tecnologias/{id}/tecnologias")
                        .build(capacidadId))
                .header(Constants.X_MESSAGE_ID, "12345")
                .retrieve()
                .bodyToFlux(TecnologiaSummaryDTO.class)
                .map(dto -> new TecnologiaSummary(dto.id(), dto.nombre()));
    }
}
