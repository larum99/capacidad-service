package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.model.CapacidadBootcamp;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
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
    private CapacidadBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = Mockito.mock(CapacidadBootcampPersistencePort.class);
        useCase = new CapacidadBootcampUseCase(persistencePort);
    }

    @Test
    void registrarCapacidadBootcamps_Success() {
        // Arrange
        CapacidadBootcamp cb1 = new CapacidadBootcamp(1L, 1L, 10L);
        CapacidadBootcamp cb2 = new CapacidadBootcamp(2L, 2L, 20L);
        List<CapacidadBootcamp> list = List.of(cb1, cb2);

        when(persistencePort.saveCapacidadBootcamp(any(CapacidadBootcamp.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act
        Flux<CapacidadBootcamp> result = useCase.registrarCapacidadBootcamps(list, "msg-1");

        // Assert
        StepVerifier.create(result)
                .expectNext(cb1)
                .expectNext(cb2)
                .verifyComplete();

        verify(persistencePort, times(2)).saveCapacidadBootcamp(any(CapacidadBootcamp.class));
    }

    @Test
    void registrarCapacidadBootcamps_EmptyList() {
        // Arrange
        List<CapacidadBootcamp> list = List.of();

        // Act
        Flux<CapacidadBootcamp> result = useCase.registrarCapacidadBootcamps(list, "msg-2");

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(persistencePort, never()).saveCapacidadBootcamp(any());
    }
}
