package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadSummaryDTO;
import com.onclass.capacidad.infrastructure.entrypoints.dto.TecnologiaSummaryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CapacidadBootcampUseCaseTest {

    private CapacidadBootcampPersistencePort persistencePort;
    private TecnologiaClientPort tecnologiaClientPort;
    private CapacidadBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = Mockito.mock(CapacidadBootcampPersistencePort.class);
        tecnologiaClientPort = Mockito.mock(TecnologiaClientPort.class);
        useCase = new CapacidadBootcampUseCase(persistencePort, tecnologiaClientPort);
    }

    @Test
    void registrarCapacidadBootcamps_shouldSaveAllRelaciones() {
        CapacidadBootcamp cb1 = new CapacidadBootcamp(1L, 1L, 10L);
        CapacidadBootcamp cb2 = new CapacidadBootcamp(2L, 2L, 20L);

        when(persistencePort.saveCapacidadBootcamp(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.registrarCapacidadBootcamps(List.of(cb1, cb2), "msg-1"))
                .expectNextCount(2)
                .verifyComplete();

        verify(persistencePort, times(2)).saveCapacidadBootcamp(any());
    }

    @Test
    void registrarCapacidadBootcamps_shouldHandleEmptyList() {
        StepVerifier.create(useCase.registrarCapacidadBootcamps(List.of(), "msg-2"))
                .verifyComplete();

        verify(persistencePort, never()).saveCapacidadBootcamp(any());
    }

    @Test
    void listarCapacidadesPorBootcamp_shouldReturnCapacidades() {
        Long bootcampId = 1L;

        // Mock del port de capacidades
        CapacidadSummaryDTO c1 = new CapacidadSummaryDTO(1L, "Java", List.of());
        CapacidadSummaryDTO c2 = new CapacidadSummaryDTO(2L, "Spring", List.of());

        when(persistencePort.findCapacidadesByBootcampId(bootcampId))
                .thenReturn(Flux.just(c1, c2));

        // Mock del port de tecnologías
        when(tecnologiaClientPort.findTecnologiasByCapacidadId(1L))
                .thenReturn(Flux.just(
                        new TecnologiaSummaryDTO(101L, "Java"),
                        new TecnologiaSummaryDTO(102L, "Java 17")
                ));
        when(tecnologiaClientPort.findTecnologiasByCapacidadId(2L))
                .thenReturn(Flux.just(
                        new TecnologiaSummaryDTO(201L, "Spring Boot")
                ));

        // Ejecutar y verificar
        StepVerifier.create(useCase.listarCapacidadesPorBootcamp(bootcampId))
                .expectNextMatches(dto -> dto.id().equals(1L)
                        && dto.nombre().equals("Java")
                        && dto.tecnologias().size() == 2)
                .expectNextMatches(dto -> dto.id().equals(2L)
                        && dto.nombre().equals("Spring")
                        && dto.tecnologias().size() == 1)
                .verifyComplete();

        // Verificar llamadas a los ports
        verify(persistencePort).findCapacidadesByBootcampId(bootcampId);
        verify(tecnologiaClientPort).findTecnologiasByCapacidadId(1L);
        verify(tecnologiaClientPort).findTecnologiasByCapacidadId(2L);
    }

    @Test
    void listarCapacidadesPorBootcamp_shouldReturnEmptyFluxIfNone() {
        Long bootcampId = 99L;

        when(persistencePort.findCapacidadesByBootcampId(bootcampId))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.listarCapacidadesPorBootcamp(bootcampId))
                .verifyComplete();

        verify(tecnologiaClientPort, never()).findTecnologiasByCapacidadId(any());
    }
}
