package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.impl;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CustomCapacidadRepository;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.EntityConstants;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util.QueryConstants;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomCapacidadRepositoryImpl implements CustomCapacidadRepository {

    private final DatabaseClient databaseClient;

    public CustomCapacidadRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<CapacidadEntity> findAllByFilters(CapacidadCriteria criteria) {
        StringBuilder sql = new StringBuilder(QueryConstants.SELECT_CAPACIDADES_BASE);

        if (QueryConstants.SORT_BY_NOMBRE.equalsIgnoreCase(criteria.getSortBy())) {
            sql.append(QueryConstants.ORDER_BY_NOMBRE)
                    .append(getValidSortOrder(criteria.getSortOrder()));
        }

        if (QueryConstants.SORT_BY_NOMBRE.equalsIgnoreCase(criteria.getSortBy())) {
            sql.append(String.format(QueryConstants.LIMIT_OFFSET, criteria.getSize(), criteria.getPage() * criteria.getSize()));
        }

        return databaseClient.sql(sql.toString())
                .map((row, metadata) -> {
                    CapacidadEntity entity = new CapacidadEntity();
                    entity.setId(row.get(QueryConstants.COLUMN_ID, Long.class));
                    entity.setNombre(row.get(EntityConstants.COLUMN_NOMBRE, String.class));
                    entity.setDescripcion(row.get(EntityConstants.COLUMN_DESCRIPCION, String.class));
                    return entity;
                })
                .all();
    }

    @Override
    public Mono<Long> countByFilters(CapacidadCriteria criteria) {
        return databaseClient.sql(QueryConstants.COUNT_CAPACIDADES)
                .map((row, metadata) -> row.get(QueryConstants.COLUMN_TOTAL, Long.class))
                .one();
    }

    private String getValidSortOrder(String sortOrder) {
        if (sortOrder != null && sortOrder.equalsIgnoreCase(QueryConstants.SORT_ORDER_DESC)) {
            return QueryConstants.SORT_ORDER_DESC;
        }
        return QueryConstants.SORT_ORDER_ASC;
    }
}