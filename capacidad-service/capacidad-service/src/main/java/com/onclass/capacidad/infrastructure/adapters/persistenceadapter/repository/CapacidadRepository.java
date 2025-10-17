package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository;

import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.entity.CapacidadEntity;
import org.springframework.data.r2dbc.repository.Modifying; // Importante
import org.springframework.data.r2dbc.repository.Query;    // Importante
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List; // Importante

@Repository
public interface CapacidadRepository extends ReactiveCrudRepository<CapacidadEntity, Long>, CustomCapacidadRepository {

    Mono<CapacidadEntity> findByNombre(String nombre);

    // ========== MÉTODO NUEVO AÑADIDO ==========

    /**
     * Elimina todas las capacidades que coincidan con la lista de IDs proporcionada.
     */
    @Modifying // 👈 Esencial para que Spring Data ejecute la modificación
    @Query("DELETE FROM capacidades WHERE id IN (:capacidadIds)")
    Mono<Void> deleteByIds(List<Long> capacidadIds);
}