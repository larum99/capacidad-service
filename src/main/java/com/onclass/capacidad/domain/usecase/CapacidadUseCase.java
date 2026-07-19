package com.onclass.capacidad.domain.usecase;

import com.onclass.capacidad.domain.api.CapacidadServicePort;
import com.onclass.capacidad.domain.constants.Constants;
import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.domain.enums.TechnicalMessage;
import com.onclass.capacidad.domain.exceptions.BusinessException;
import com.onclass.capacidad.domain.model.Capacidad;
import com.onclass.capacidad.domain.model.CapacidadConTecnologias;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.utils.PageResult;
import com.onclass.capacidad.domain.utils.TecnologiaSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CapacidadUseCase implements CapacidadServicePort {

    private final CapacidadPersistencePort capacidadPersistencePort;
    private final TecnologiaClientPort tecnologiaClientPort;

    public CapacidadUseCase(CapacidadPersistencePort capacidadPersistencePort,
                            TecnologiaClientPort tecnologiaClientPort) {
        this.capacidadPersistencePort = capacidadPersistencePort;
        this.tecnologiaClientPort = tecnologiaClientPort;
    }

    @Override
    public Mono<Capacidad> registrarCapacidad(Capacidad capacidad, String messageId) {
        if (capacidad.nombre() == null || capacidad.nombre().isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_NOMBRE_REQUIRED));
        }
        if (capacidad.nombre().length() > Constants.MAX_NOMBRE_CAPACIDAD) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_NOMBRE_TOO_LONG));
        }
        if (capacidad.descripcion() == null || capacidad.descripcion().isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_DESCRIPCION_REQUIRED));
        }
        if (capacidad.descripcion().length() > Constants.MAX_DESCRIPCION_CAPACIDAD) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_DESCRIPCION_TOO_LONG));
        }

        List<Long> tecnologias = capacidad.tecnologias();
        if (tecnologias == null || tecnologias.size() < Constants.MIN_TECNOLOGIAS) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_MIN_TECNOLOGIAS));
        }
        if (tecnologias.size() > Constants.MAX_TECNOLOGIAS) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_MAX_TECNOLOGIAS));
        }

        Set<Long> set = new HashSet<>(tecnologias);
        if (set.size() != tecnologias.size()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_TECNOLOGIAS_DUPLICADAS));
        }

        return capacidadPersistencePort.existByNombre(capacidad.nombre())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_ALREADY_EXISTS));
                    }
                    return capacidadPersistencePort.saveCapacidad(capacidad)
                            .flatMap(saved ->
                                    tecnologiaClientPort.associateCapacidadWithTecnologias(saved.id(), tecnologias)
                                            .thenReturn(saved)
                            );
                });
    }

    @Override
    public Mono<PageResult<CapacidadConTecnologias>> listarCapacidades(CapacidadCriteria criteria) {
        String sortBy = Optional.ofNullable(criteria.getSortBy()).orElse(Constants.SORT_BY_NOMBRE);

        switch (sortBy.toLowerCase()) {
            case Constants.SORT_BY_NOMBRE:
                return sortByName(criteria);
            case Constants.SORT_BY_CANTIDAD_TECNOLOGIAS:
                return sortByTechnologyCount(criteria);
            default:
                return Mono.error(new BusinessException(TechnicalMessage.INVALID_PARAMETERS));
        }
    }

    @Override
    public Mono<Void> eliminarCapacidadesPorIds(List<Long> capacidadIds) {
        return capacidadPersistencePort.deleteByIds(capacidadIds);
    }

    private Mono<PageResult<CapacidadConTecnologias>> sortByName(CapacidadCriteria criteria) {
        return capacidadPersistencePort.findAll(criteria)
                .flatMap(page -> {
                    Flux<CapacidadConTecnologias> enrichedFlux = Flux.fromIterable(page.getContent())
                            .concatMap(capacidad ->
                                    tecnologiaClientPort.findTecnologiasByCapacidadId(capacidad.id())
                                            .collectList()
                                            .map(tecnologias -> new CapacidadConTecnologias(
                                                    capacidad.id(),
                                                    capacidad.nombre(),
                                                    capacidad.descripcion(),
                                                    tecnologias // List<TecnologiaSummary>
                                            ))
                            );

                    return enrichedFlux.collectList()
                            .map(list -> new PageResult<>(
                                    list,
                                    page.getTotalElements(),
                                    page.getTotalPages(),
                                    page.getCurrentPage(),
                                    page.getPageSize(),
                                    page.isFirst(),
                                    page.isLast()
                            ));
                });
    }

    private Mono<PageResult<CapacidadConTecnologias>> sortByTechnologyCount(CapacidadCriteria criteria) {
        return capacidadPersistencePort.findAll(criteria)
                .flatMapMany(page -> Flux.fromIterable(page.getContent()))
                .concatMap(capacidad ->
                        tecnologiaClientPort.findTecnologiasByCapacidadId(capacidad.id())
                                .collectList()
                                .map(tecnologias -> new CapacidadConTecnologias(
                                        capacidad.id(),
                                        capacidad.nombre(),
                                        capacidad.descripcion(),
                                        tecnologias // List<TecnologiaSummary>
                                ))
                )
                .collectList()
                .flatMap(fullList -> {
                    Comparator<CapacidadConTecnologias> comparator = Comparator.comparingInt(c -> c.tecnologias().size());
                    if (Constants.SORT_ORDER_DESC.equalsIgnoreCase(criteria.getSortOrder())) {
                        comparator = comparator.reversed();
                    }
                    fullList.sort(comparator);

                    int totalElements = fullList.size();
                    int totalPages = (int) Math.ceil((double) totalElements / criteria.getSize());
                    int page = criteria.getPage();
                    int size = criteria.getSize();

                    int fromIndex = page * size;
                    if (fromIndex >= totalElements) {
                        return Mono.just(new PageResult<>(
                                List.of(),
                                (long) totalElements,
                                totalPages,
                                page,
                                size,
                                true,
                                true
                        ));
                    }

                    int toIndex = Math.min(fromIndex + size, totalElements);
                    List<CapacidadConTecnologias> paginatedList = fullList.subList(fromIndex, toIndex);

                    return Mono.just(new PageResult<>(
                            paginatedList,
                            (long) totalElements,
                            totalPages,
                            page,
                            size,
                            page == 0,
                            toIndex >= totalElements
                    ));
                });
    }
}
