package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.TecnologiaSummary;
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
        // Arrange
        CapacidadBootcamp cb1 = new CapacidadBootcamp(1L, 1L, 10L);
        CapacidadBootcamp cb2 = new CapacidadBootcamp(2L, 2L, 20L);
        when(persistencePort.saveCapacidadBootcamp(any()))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act & Assert
        StepVerifier.create(useCase.registrarCapacidadBootcamps(List.of(cb1, cb2), "msg-1"))
                .expectNextCount(2)
                .verifyComplete();

        verify(persistencePort, times(2)).saveCapacidadBootcamp(any());
    }

    @Test
    void registrarCapacidadBootcamps_shouldHandleEmptyList() {
        // Arrange & Act & Assert
        StepVerifier.create(useCase.registrarCapacidadBootcamps(List.of(), "msg-2"))
                .verifyComplete();

        verify(persistencePort, never()).saveCapacidadBootcamp(any());
    }

    @Test
    void listarCapacidadesPorBootcamp_shouldReturnCapacidades() {
        // Arrange
        Long bootcampId = 1L;
        Capacidad c1 = new Capacidad(1L, "Java", "Programación Java", List.of());
        Capacidad c2 = new Capacidad(2L, "Spring", "Framework Spring", List.of());

        when(persistencePort.findCapacidadesByBootcampId(bootcampId))
                .thenReturn(Flux.just(c1, c2));
        when(tecnologiaClientPort.findTecnologiasByCapacidadId(1L))
                .thenReturn(Flux.just(
                        new TecnologiaSummary(101L, "Java"),
                        new TecnologiaSummary(102L, "Java 17")
                ));
        when(tecnologiaClientPort.findTecnologiasByCapacidadId(2L))
                .thenReturn(Flux.just(
                        new TecnologiaSummary(201L, "Spring Boot")
                ));

        // Act & Assert
        StepVerifier.create(useCase.listarCapacidadesPorBootcamp(bootcampId))
                .expectNextMatches(dto -> dto.id().equals(1L)
                        && dto.nombre().equals("Java")
                        && dto.tecnologias().size() == 2)
                .expectNextMatches(dto -> dto.id().equals(2L)
                        && dto.nombre().equals("Spring")
                        && dto.tecnologias().size() == 1)
                .verifyComplete();

        verify(persistencePort).findCapacidadesByBootcampId(bootcampId);
        verify(tecnologiaClientPort).findTecnologiasByCapacidadId(1L);
        verify(tecnologiaClientPort).findTecnologiasByCapacidadId(2L);
    }

    @Test
    void listarCapacidadesPorBootcamp_shouldReturnEmptyFluxIfNone() {
        // Arrange
        Long bootcampId = 99L;
        when(persistencePort.findCapacidadesByBootcampId(bootcampId))
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(useCase.listarCapacidadesPorBootcamp(bootcampId))
                .verifyComplete();

        verify(tecnologiaClientPort, never()).findTecnologiasByCapacidadId(any());
    }

    @Test
    void eliminarCapacidadesPorBootcamp_exito() {
        // Arrange
        Long bootcampId = 1L;
        List<Long> deletedIds = List.of(1L, 2L);
        when(persistencePort.deleteByBootcampId(bootcampId)).thenReturn(Mono.just(deletedIds));

        // Act & Assert
        StepVerifier.create(useCase.eliminarCapacidadesPorBootcamp(bootcampId, "msg-1"))
                .expectNext(deletedIds)
                .verifyComplete();
    }

    @Test
    void countBootcampsByCapacidadId_exito() {
        // Arrange
        Long capacidadId = 1L;
        when(persistencePort.countBootcampsByCapacidadId(capacidadId)).thenReturn(Mono.just(5));

        // Act & Assert
        StepVerifier.create(useCase.countBootcampsByCapacidadId(capacidadId))
                .expectNext(5)
                .verifyComplete();
    }
}
