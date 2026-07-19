# Bootcamp - Capacidad Microservice

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![WebFlux](https://img.shields.io/badge/WebFlux-Reactive-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![Gradle](https://img.shields.io/badge/Gradle-8.14-02303A?style=flat-square&logo=gradle&logoColor=white)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Descripcion

Microservicio de gestion de capacidades para la plataforma **Bootcamp**. Permite crear, listar y eliminar capacidades, asi como gestionar la asociacion entre capacidades y bootcamps. Consulta tecnologias asociadas desde el microservicio `tecnologia-service`.

**Puerto:** `8090`
**Base Path:** `/capacidad-service`

## Stack Tecnologico

| Componente | Tecnologia |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Web | Spring WebFlux (Reactiva) |
| Base de datos | MySQL via R2DBC |
| Pool de conexiones | R2DBC Pool |
| Build Tool | Gradle |
| Mapeo | MapStruct 1.5.5 + Lombok |
| Documentacion API | SpringDoc OpenAPI 2.6.0 (WebFlux) |
| Resiliencia | Resilience4j (CircuitBreaker, Retry, Bulkhead) |
| Monitoreo | Spring Boot Actuator |
| Testing | JUnit 5 + Mockito + Reactor Test |

## Arquitectura

Arquitectura Hexagonal (Puertos y Adaptadores) con stack reactivo:

```
com.onclass.capacidad
├── application/
│   ├── config/                    # Configuracion (UseCases, WebClient)
│   └── configSwagger/             # Configuracion OpenAPI/Swagger
├── domain/
│   ├── api/                       # Puertos entrantes (CapacidadServicePort, CapacidadBootcampServicePort)
│   ├── spi/                       # Puertos salientes (Persistence ports, TecnologiaClientPort)
│   ├── model/                     # Modelos de dominio (Capacidad, CapacidadBootcamp)
│   ├── usecase/                   # Casos de uso (CapacidadUseCase, CapacidadBootcampUseCase)
│   ├── constants/                 # Constantes de dominio
│   ├── criteria/                  # Criterios de busqueda
│   ├── enums/                     # Mensajes tecnicos
│   ├── exceptions/                # Excepciones de dominio
│   └── utils/                     # PageResult, TecnologiaSummary
└── infrastructure/
    ├── entrypoints/
    │   ├── RouterRest             # Rutas funcionales WebFlux
    │   ├── handler/               # CapacidadHandlerImpl, CapacidadBootcampHandlerImpl
    │   ├── dto/                   # CapacidadDTO, BootcampCapacidadDTO
    │   ├── mapper/                # CapacidadMapper, CapacidadBootcampMapper
    │   └── util/                  # Constants, APIResponse, ErrorDTO
    └── adapters/
        ├── client/
        │   └── TecnologiaClientAdapter   # Cliente HTTP a tecnologia-service
        └── persistenceadapter/
            ├── entity/            # CapacidadEntity, CapacidadBootcampEntity
            ├── repository/        # CapacidadRepository, CapacidadBootcampRepository
            ├── mapper/            # CapacidadEntityMapper, CapacidadBootcampEntityMapper
            └── util/              # EntityConstants, QueryConstants, MapperConstants
```

## Endpoints

Todos los endpoints requieren el header `x-message-id` para trazabilidad.

### Capacidades

| Metodo | Ruta | Descripcion |
|---|---|---|
| `POST` | `/capacidad-service/capacidades` | Crear una nueva capacidad |
| `GET` | `/capacidad-service/capacidades` | Listar capacidades (paginado) |
| `DELETE` | `/capacidad-service/capacidades` | Eliminar capacidades por lista de IDs |
| `POST` | `/capacidad-service/capacidades/validate` | Validar que capacidades existan (interno) |

### Capacidad - Bootcamps

| Metodo | Ruta | Descripcion |
|---|---|---|
| `POST` | `/capacidad-service/capacidad-bootcamps` | Asociar capacidades a un bootcamp |
| `GET` | `/capacidad-service/capacidad-bootcamps/{bootcampId}/capacidades` | Listar capacidades de un bootcamp |
| `DELETE` | `/capacidad-service/capacidad-bootcamps/{bootcampId}` | Eliminar asociaciones de un bootcamp |
| `GET` | `/capacidad-service/capacidades/{capacidadId}/bootcamps/count` | Contar bootcamps de una capacidad |

### Request - Crear Capacidad

```json
{
  "nombre": "Backend Java",
  "descripcion": "Desarrollo de servicios REST con Spring Boot",
  "tecnologias": [1, 2, 3]
}
```

**Response (201 Created):**
```json
{
  "code": "201",
  "message": "Capacidad creada exitosamente",
  "identifier": "msg-uuid-123",
  "date": "2026-07-19T10:30:00Z"
}
```

### Request - Asociar Capacidades a Bootcamp

```json
[
  { "bootcampId": 1, "capacidadId": 1 },
  { "bootcampId": 1, "capacidadId": 2 }
]
```

**Response (201 Created):**
```json
[
  { "id": 1, "capacidadId": 1, "bootcampId": 1 },
  { "id": 2, "capacidadId": 2, "bootcampId": 1 }
]
```

### Request - Listar Capacidades (paginado)

```
GET /capacidad-service/capacidades?page=0&size=10&sortBy=nombre&sortOrder=asc
```

**Response (200 OK):**
```json
{
  "data": [
    {
      "id": 1,
      "nombre": "Backend Java",
      "descripcion": "Desarrollo de servicios REST con Spring Boot",
      "tecnologias": [
        { "id": 1, "nombre": "Spring Boot" },
        { "id": 2, "nombre": "Java 17" }
      ]
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0
}
```

### Respuesta de Error

```json
{
  "code": "400-2",
  "message": "La capacidad ya esta registrada",
  "identifier": "msg-uuid-123",
  "date": "2026-07-19T10:30:00Z",
  "errors": [
    {
      "code": "400-2",
      "message": "La capacidad ya esta registrada",
      "param": "nombre"
    }
  ]
}
```

## Modelo de Datos

```
┌──────────────────┐       ┌──────────────────────┐
│   capacidades    │       │  capacidad_bootcamp  │
├──────────────────┤       ├──────────────────────┤
│ id (PK)          │◄──FK──│ id_capacidad (FK)    │
│ nombre           │       │ id (PK)              │
│ descripcion      │       │ id_bootcamp          │
└──────────────────┘       └──────────────────────┘
```

## Variables de Entorno

| Variable | Descripcion | Ejemplo |
|---|---|---|
| `DB_HOST` | Host de MySQL | `localhost` |
| `DB_PORT` | Puerto de MySQL | `3306` |
| `DB_NAME` | Nombre de la base de datos | `capacidad` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contrasena de MySQL | `password` |

**Base de datos por defecto:** `capacidad`

## Servicios Externos

| Servicio | URL | Uso |
|---|---|---|
| `tecnologia-service` | `http://localhost:8080/tecnologia-service` | Consulta y asociacion de tecnologias |

## Resiliencia

| Patron | Nombre | Configuracion |
|---|---|---|
| CircuitBreaker | `capacidadDB` | Proteccion contra fallos de DB |
| Retry | `capacidadRetry` | Max 5 intentos, backoff exponencial |
| Bulkhead | `capacidadBulkhead` | Max 5 llamadas concurrentes |

## Actuator

```
http://localhost:8090/capacidad-service/actuator/health
http://localhost:8090/capacidad-service/actuator/metrics
http://localhost:8090/capacidad-service/actuator/loggers
```

## Ejecutar el Proyecto

```bash
cd capacidad-service
./gradlew bootRun
```

> **Requisito:** MySQL debe estar ejecutandose en `localhost:3306`

### 1. Crear la base de datos

Ejecuta en MySQL el script `init.sql`:

```bash
mysql -u root -p < src/main/resources/init.sql
```

o ejecuta manualmente:

```sql
CREATE DATABASE IF NOT EXISTS capacidad;
```

### 2. Iniciar la aplicacion

Las tablas se crean automaticamente al iniciar la app (via `schema.sql`).

La aplicacion estara disponible en `http://localhost:8090`

## Documentacion API (Swagger)

```
http://localhost:8090/swagger-ui/index.html
http://localhost:8090/v3/api-docs
```

## Ejecutar Tests

```bash
./gradlew test
```

## Reglas de Negocio

- Una capacidad debe tener nombre unico
- El nombre de la capacidad no puede superar 50 caracteres
- La descripcion de la capacidad no puede superar 90 caracteres
- Una capacidad debe tener al menos 3 tecnologias asociadas
- Una capacidad no puede tener mas de 20 tecnologias asociadas
- No se permiten tecnologias duplicadas en una capacidad
- Las tecnologias asociadas deben existir en `tecnologia-service`
- Al eliminar un bootcamp, se eliminan todas sus asociaciones con capacidades
- Las capacidades compartidas con otros bootcamps no se eliminan
