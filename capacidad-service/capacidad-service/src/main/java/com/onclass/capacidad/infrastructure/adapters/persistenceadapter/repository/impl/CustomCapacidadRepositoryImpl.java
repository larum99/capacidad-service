package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.impl;

import com.onclass.capacidad.domain.criteria.CapacidadCriteria;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CustomCapacidadRepository;
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
        StringBuilder sql = new StringBuilder("""
                SELECT c.id, c.nombre, c.descripcion
                FROM capacidades c
                """);

        // 🔹 En caso de que luego quieras filtrar por nombre
        boolean hasFilter = false;
        if (criteria.getSortBy() != null && criteria.getSortBy().equalsIgnoreCase("nombre")) {
            sql.append("WHERE c.nombre IS NOT NULL ");
            hasFilter = true;
        }

        // 🔹 Ordenamiento
        sql.append("ORDER BY c.")
                .append(criteria.getSortBy() != null ? criteria.getSortBy() : "nombre")
                .append(" ")
                .append(criteria.getSortOrder() != null ? criteria.getSortOrder() : "ASC");

        // 🔹 Paginación
        sql.append(" LIMIT ").append(criteria.getSize())
                .append(" OFFSET ").append(criteria.getPage() * criteria.getSize());

        return databaseClient.sql(sql.toString())
                .map((row, metadata) -> {
                    CapacidadEntity entity = new CapacidadEntity();
                    entity.setId(row.get("id", Long.class));
                    entity.setNombre(row.get("nombre", String.class));
                    entity.setDescripcion(row.get("descripcion", String.class));
                    return entity;
                })
                .all();
    }

    @Override
    public Mono<Long> countByFilters(CapacidadCriteria criteria) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM capacidades c");

        // 🔹 Si en el futuro se agrega un filtro por nombre, solo se añade aquí
        // Ejemplo:
        // if (criteria.getNombre() != null && !criteria.getNombre().isBlank()) {
        //     sql.append(" WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))");
        // }

        return databaseClient.sql(sql.toString())
                .map((row, metadata) -> row.get("total", Long.class))
                .one();
    }
}
