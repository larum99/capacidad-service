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
        return validarCapacidad(capacidad)
                .then(verificarCapacidadNoExiste(capacidad.nombre()))
                .then(guardarCapacidadConTecnologias(capacidad));
    }

    private Mono<Void> validarCapacidad(Capacidad capacidad) {
        return validarNombre(capacidad.nombre())
                .then(validarDescripcion(capacidad.descripcion()))
                .then(validarTecnologias(capacidad.tecnologias()));
    }

    private Mono<Void> validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_NOMBRE_REQUIRED));
        }
        if (nombre.length() > Constants.MAX_NOMBRE_CAPACIDAD) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_NOMBRE_TOO_LONG));
        }
        return Mono.empty();
    }

    private Mono<Void> validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_DESCRIPCION_REQUIRED));
        }
        if (descripcion.length() > Constants.MAX_DESCRIPCION_CAPACIDAD) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_DESCRIPCION_TOO_LONG));
        }
        return Mono.empty();
    }

    private Mono<Void> validarTecnologias(List<Long> tecnologias) {
        return validarCantidadTecnologias(tecnologias)
                .then(validarTecnologiasNoDuplicadas(tecnologias));
    }

    private Mono<Void> validarCantidadTecnologias(List<Long> tecnologias) {
        if (tecnologias == null || tecnologias.size() < Constants.MIN_TECNOLOGIAS) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_MIN_TECNOLOGIAS));
        }
        if (tecnologias.size() > Constants.MAX_TECNOLOGIAS) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_MAX_TECNOLOGIAS));
        }
        return Mono.empty();
    }

    private Mono<Void> validarTecnologiasNoDuplicadas(List<Long> tecnologias) {
        Set<Long> set = new HashSet<>(tecnologias);
        if (set.size() != tecnologias.size()) {
            return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_TECNOLOGIAS_DUPLICADAS));
        }
        return Mono.empty();
    }

    private Mono<Void> verificarCapacidadNoExiste(String nombre) {
        return capacidadPersistencePort.existByNombre(nombre)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException(TechnicalMessage.CAPACIDAD_ALREADY_EXISTS));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Capacidad> guardarCapacidadConTecnologias(Capacidad capacidad) {
        return capacidadPersistencePort.saveCapacidad(capacidad)
                .flatMap(saved -> asociarTecnologias(saved, capacidad.tecnologias()));
    }

    private Mono<Capacidad> asociarTecnologias(Capacidad capacidad, List<Long> tecnologias) {
        return tecnologiaClientPort.associateCapacidadWithTecnologias(capacidad.id(), tecnologias)
                .thenReturn(capacidad);
    }

    @Override
    public Mono<PageResult<CapacidadConTecnologias>> listarCapacidades(CapacidadCriteria criteria) {
        String sortBy = determinarCriterioOrdenamiento(criteria.getSortBy());
        return aplicarOrdenamiento(criteria, sortBy);
    }

    private String determinarCriterioOrdenamiento(String sortBy) {
        return Optional.ofNullable(sortBy).orElse(Constants.SORT_BY_NOMBRE);
    }

    private Mono<PageResult<CapacidadConTecnologias>> aplicarOrdenamiento(CapacidadCriteria criteria, String sortBy) {
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

    @Override
    public Mono<Boolean> validateCapacidadesExist(List<Long> capacidadIds) {
        return capacidadPersistencePort.existsByIds(capacidadIds);
    }

    private Mono<PageResult<CapacidadConTecnologias>> sortByName(CapacidadCriteria criteria) {
        return capacidadPersistencePort.findAll(criteria)
                .flatMap(this::enriquecerCapacidadesConTecnologias);
    }

    private Mono<PageResult<CapacidadConTecnologias>> enriquecerCapacidadesConTecnologias(PageResult<Capacidad> page) {
        Flux<CapacidadConTecnologias> enrichedFlux = Flux.fromIterable(page.getContent())
                .concatMap(this::crearCapacidadConTecnologias);

        return enrichedFlux.collectList()
                .map(list -> crearPageResult(list, page));
    }

    private Mono<CapacidadConTecnologias> crearCapacidadConTecnologias(Capacidad capacidad) {
        return tecnologiaClientPort.findTecnologiasByCapacidadId(capacidad.id())
                .collectList()
                .map(tecnologias -> new CapacidadConTecnologias(
                        capacidad.id(),
                        capacidad.nombre(),
                        capacidad.descripcion(),
                        tecnologias
                ));
    }

    private PageResult<CapacidadConTecnologias> crearPageResult(List<CapacidadConTecnologias> content, PageResult<Capacidad> originalPage) {
        return new PageResult<>(
                content,
                originalPage.getTotalElements(),
                originalPage.getTotalPages(),
                originalPage.getCurrentPage(),
                originalPage.getPageSize(),
                originalPage.isFirst(),
                originalPage.isLast()
        );
    }

    private Mono<PageResult<CapacidadConTecnologias>> sortByTechnologyCount(CapacidadCriteria criteria) {
        return capacidadPersistencePort.findAll(criteria)
                .flatMapMany(page -> Flux.fromIterable(page.getContent()))
                .concatMap(this::crearCapacidadConTecnologias)
                .collectList()
                .flatMap(fullList -> aplicarOrdenamientoPorCantidadTecnologias(fullList, criteria));
    }

    private Mono<PageResult<CapacidadConTecnologias>> aplicarOrdenamientoPorCantidadTecnologias(List<CapacidadConTecnologias> fullList, CapacidadCriteria criteria) {
        ordenarPorCantidadTecnologias(fullList, criteria.getSortOrder());
        return paginarResultados(fullList, criteria);
    }

    private void ordenarPorCantidadTecnologias(List<CapacidadConTecnologias> list, String sortOrder) {
        Comparator<CapacidadConTecnologias> comparator = Comparator.comparingInt(c -> c.tecnologias().size());
        if (Constants.SORT_ORDER_DESC.equalsIgnoreCase(sortOrder)) {
            comparator = comparator.reversed();
        }
        list.sort(comparator);
    }

    private Mono<PageResult<CapacidadConTecnologias>> paginarResultados(List<CapacidadConTecnologias> fullList, CapacidadCriteria criteria) {
        int totalElements = fullList.size();
        int totalPages = calcularTotalPaginas(totalElements, criteria.getSize());
        
        if (esPaginaFueraDeRango(criteria.getPage(), criteria.getSize(), totalElements)) {
            return Mono.just(crearPageResultVacio(totalElements, totalPages, criteria));
        }

        List<CapacidadConTecnologias> paginatedList = extraerPaginaActual(fullList, criteria);
        return Mono.just(crearPageResultConDatos(paginatedList, totalElements, totalPages, criteria));
    }

    private int calcularTotalPaginas(int totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }

    private boolean esPaginaFueraDeRango(int page, int size, int totalElements) {
        return page * size >= totalElements;
    }

    private PageResult<CapacidadConTecnologias> crearPageResultVacio(int totalElements, int totalPages, CapacidadCriteria criteria) {
        return new PageResult<>(
                List.of(),
                (long) totalElements,
                totalPages,
                criteria.getPage(),
                criteria.getSize(),
                true,
                true
        );
    }

    private List<CapacidadConTecnologias> extraerPaginaActual(List<CapacidadConTecnologias> fullList, CapacidadCriteria criteria) {
        int fromIndex = criteria.getPage() * criteria.getSize();
        int toIndex = Math.min(fromIndex + criteria.getSize(), fullList.size());
        return fullList.subList(fromIndex, toIndex);
    }

    private PageResult<CapacidadConTecnologias> crearPageResultConDatos(List<CapacidadConTecnologias> content, int totalElements, int totalPages, CapacidadCriteria criteria) {
        return new PageResult<>(
                content,
                (long) totalElements,
                totalPages,
                criteria.getPage(),
                criteria.getSize(),
                criteria.getPage() == 0,
                content.size() + criteria.getPage() * criteria.getSize() >= totalElements
        );
    }
}
