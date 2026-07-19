package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.constants.Constants;
import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.PageResult;
import com.onclass.capacidad.infrastructure.entrypoints.dto.CapacidadListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CapacidadUseCaseTest {

    @Mock
    private CapacidadPersistencePort capacidadPersistencePort;

    @Mock
    private TecnologiaClientPort tecnologiaClientPort;

    @InjectMocks
    private CapacidadUseCase capacidadUseCase;

    private Capacidad capacidadValida;
    private CapacidadListDTO dto1;
    private CapacidadListDTO dto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        capacidadValida = new Capacidad(
                1L,
                "Capacidad Backend",
                "Desarrollo de microservicios con Spring WebFlux",
                List.of(1L, 2L, 3L)
        );

        dto1 = new CapacidadListDTO(1L, "Backend", "Microservicios", List.of());
        dto2 = new CapacidadListDTO(2L, "Frontend", "React y Angular", List.of());
    }

    @Test
    void registrarCapacidad_exito() {
        when(capacidadPersistencePort.existByNombre(anyString())).thenReturn(Mono.just(false));
        when(capacidadPersistencePort.saveCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidadValida));
        when(tecnologiaClientPort.associateCapacidadWithTecnologias(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(capacidadUseCase.registrarCapacidad(capacidadValida, "msg-123"))
                .expectNext(capacidadValida)
                .verifyComplete();
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
    void registrarCapacidad_masDeVeinteTecnologias() {
        List<Long> muchas = LongStream.range(1, 25).boxed().toList();
        Capacidad invalida = new Capacidad(1L, "Capacidad", "desc", muchas);

        StepVerifier.create(capacidadUseCase.registrarCapacidad(invalida, "msg"))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.CAPACIDAD_MAX_TECNOLOGIAS.getDescription()))
                .verify();
    }

    @Test
    void listarCapacidades_ordenPorNombre_exito() {
        PageResult<CapacidadListDTO> page = new PageResult<>(List.of(dto1, dto2), 2L, 1, 0, 10, true, true);

        when(capacidadPersistencePort.findAll(any(CapacidadCriteria.class))).thenReturn(Mono.just(page));
        when(tecnologiaClientPort.findTecnologiasByCapacidadId(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    if (id == 1L) {
                        return Flux.just(1L, 2L);
                    } else if (id == 2L) {
                        return Flux.just(3L);
                    }
                    return Flux.empty();
                });

        CapacidadCriteria criteria = new CapacidadCriteria();
        criteria.setSortBy(Constants.SORT_BY_NOMBRE);
        criteria.setPage(0);
        criteria.setSize(10);

        StepVerifier.create(capacidadUseCase.listarCapacidades(criteria))
                .expectNextMatches(result -> result.getContent().size() == 2 &&
                        result.getContent().get(0).id() == 1L &&
                        result.getContent().get(0).tecnologias().size() == 2 &&
                        result.getContent().get(1).id() == 2L &&
                        result.getContent().get(1).tecnologias().size() == 1
                )
                .verifyComplete();
    }

    @Test
    void listarCapacidades_ordenPorCantidadTecnologias_descendente() {
        PageResult<CapacidadListDTO> page = new PageResult<>(List.of(dto1, dto2), 2L, 1, 0, 10, true, true);

        when(capacidadPersistencePort.findAll(any(CapacidadCriteria.class))).thenReturn(Mono.just(page));
        when(tecnologiaClientPort.findTecnologiasByCapacidadId(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    if (id == 1L) {
                        return Flux.just(1L, 2L, 3L);
                    } else if (id == 2L) {
                        return Flux.just(1L);
                    }
                    return Flux.empty();
                });


        CapacidadCriteria criteria = new CapacidadCriteria();
        criteria.setSortBy(Constants.SORT_BY_CANTIDAD_TECNOLOGIAS);
        criteria.setSortOrder(Constants.SORT_ORDER_DESC);
        criteria.setPage(0);
        criteria.setSize(10);

        StepVerifier.create(capacidadUseCase.listarCapacidades(criteria))
                .expectNextMatches(result -> result.getContent().get(0).id() == 1L &&
                        result.getContent().get(1).id() == 2L
                )
                .verifyComplete();
    }

    @Test
    void listarCapacidades_parametroSortInvalido() {
        CapacidadCriteria criteria = new CapacidadCriteria();
        criteria.setSortBy("invalido");

        StepVerifier.create(capacidadUseCase.listarCapacidades(criteria))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        ((BusinessException) e).getMessage().equals(TechnicalMessage.INVALID_SORT_BY_PARAM.getDescription()))
                .verify();
    }
}
