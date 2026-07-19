package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.constants.Constants;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CapacidadUseCaseTest {

    @Mock
    private CapacidadPersistencePort capacidadPersistencePort;

    @Mock
    private TecnologiaClientPort tecnologiaClientPort;

    @InjectMocks
    private CapacidadUseCase capacidadUseCase;

    private Capacidad capacidadValida;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        capacidadValida = new Capacidad(
                1L,
                "Capacidad Backend",
                "Desarrollo de microservicios con Spring WebFlux",
                List.of(1L, 2L, 3L)
        );
    }

    @Test
    void registrarCapacidad_exito() {
        when(capacidadPersistencePort.existByNombre(anyString())).thenReturn(Mono.just(false));
        when(capacidadPersistencePort.saveCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidadValida));
        when(tecnologiaClientPort.associateCapacidadWithTecnologias(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(capacidadUseCase.registrarCapacidad(capacidadValida, "msg-123"))
                .expectNext(capacidadValida)
                .verifyComplete();

        verify(capacidadPersistencePort).existByNombre("Capacidad Backend");
        verify(capacidadPersistencePort).saveCapacidad(capacidadValida);
        verify(tecnologiaClientPort).associateCapacidadWithTecnologias(1L, List.of(1L, 2L, 3L));
    }

    @Test
    void registrarCapacidad_nombreRequerido() {
        Capacidad invalida = new Capacidad(1L, " ", "desc", List.of(1L, 2L, 3L));

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_NOMBRE_REQUIRED.getDescription()))
                .verify();
    }

    @Test
    void registrarCapacidad_nombreMuyLargo() {
        String nombreLargo = "A".repeat(Constants.MAX_NOMBRE_CAPACIDAD + 1);
        Capacidad invalida = new Capacidad(1L, nombreLargo, "desc", List.of(1L, 2L, 3L));

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_NOMBRE_TOO_LONG.getDescription()))
                .verify();
    }

    @Test
    void registrarCapacidad_descripcionRequerida() {
        Capacidad invalida = new Capacidad(1L, "Capacidad", " ", List.of(1L, 2L, 3L));

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_DESCRIPCION_REQUIRED.getDescription()))
                .verify();
    }

    @Test
    void registrarCapacidad_menosDeTresTecnologias() {
        Capacidad invalida = new Capacidad(1L, "Capacidad", "desc", List.of(1L));

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_MIN_TECNOLOGIAS.getDescription()))
                .verify();
    }

    @Test
    void registrarCapacidad_masDeVeinteTecnologias() {
        List<Long> muchas = LongStream.range(1, 25).boxed().toList();
        Capacidad invalida = new Capacidad(1L, "Capacidad", "desc", muchas);

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_MAX_TECNOLOGIAS.getDescription()))
                .verify();
    }

    @Test
    void registrarCapacidad_tecnologiasDuplicadas() {
        Capacidad invalida = new Capacidad(1L, "Capacidad", "desc", List.of(1L, 1L, 2L));

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_TECNOLOGIAS_DUPLICADAS.getDescription()))
                .verify();
    }

    @Test
    void registrarCapacidad_yaExisteNombre() {
        when(capacidadPersistencePort.existByNombre(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(capacidadUseCase.registrarCapacidad(capacidadValida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_ALREADY_EXISTS.getDescription()))
                .verify();
    }
}
