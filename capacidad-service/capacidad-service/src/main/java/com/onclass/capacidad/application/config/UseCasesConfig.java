package com.onclass.capacidad.application.config;

import com.onclass.capacidad.domain.api.CapacidadBootcampServicePort;
import com.onclass.capacidad.domain.api.CapacidadServicePort;
import com.onclass.capacidad.domain.spi.CapacidadBootcampPersistencePort;
import com.onclass.capacidad.domain.spi.CapacidadPersistencePort;
import com.onclass.capacidad.domain.spi.TecnologiaClientPort;
import com.onclass.capacidad.domain.usecase.CapacidadBootcampUseCase;
import com.onclass.capacidad.domain.usecase.CapacidadUseCase;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.CapacidadBootcampPersistenceAdapter;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.CapacidadPersistenceAdapter;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadBootcampEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.mapper.CapacidadEntityMapper;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadBootcampRepository;
import com.onclass.capacidad.infrastructure.adapters.persistenceadapter.repository.CapacidadRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public CapacidadPersistencePort capacidadPersistencePort(
            CapacidadRepository capacidadRepository,
            CapacidadEntityMapper capacidadEntityMapper) {
        return new CapacidadPersistenceAdapter(capacidadRepository, capacidadEntityMapper);
    }

    @Bean
    public CapacidadServicePort capacidadServicePort(
            CapacidadPersistencePort capacidadPersistencePort,
            TecnologiaClientPort tecnologiaClientPort) {
        return new CapacidadUseCase(capacidadPersistencePort, tecnologiaClientPort);
    }

    @Bean
    public CapacidadBootcampPersistencePort capacidadBootcampPersistencePort(
            CapacidadBootcampRepository repository,
            CapacidadBootcampEntityMapper mapper) {
        return new CapacidadBootcampPersistenceAdapter(repository, mapper);
    }

    @Bean
    public CapacidadBootcampServicePort capacidadBootcampServicePort(
            CapacidadBootcampPersistencePort persistencePort,
            TecnologiaClientPort tecnologiaClientPort) {
        return new CapacidadBootcampUseCase(persistencePort, tecnologiaClientPort);
    }
}
