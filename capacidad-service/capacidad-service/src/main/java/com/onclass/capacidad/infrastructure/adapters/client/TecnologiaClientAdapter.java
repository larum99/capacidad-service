package com.onclass.capacidad.infrastructure.adapters.client;

import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.infrastructure.entrypoints.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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
        List<CapacidadTecnologiaDTO> dtos = tecnologiasIds.stream()
                .map(techId -> new CapacidadTecnologiaDTO(capacidadId, techId))
                .collect(Collectors.toList());

        return webClient.post()
                .uri("/capacidad-tecnologias")
                .header(Constants.X_MESSAGE_ID, "12345")
                .bodyValue(dtos)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public record CapacidadTecnologiaDTO(Long capacidadId, Long tecnologiaId) {}
}
