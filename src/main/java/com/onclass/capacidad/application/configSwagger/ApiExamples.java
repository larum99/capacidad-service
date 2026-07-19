package com.onclass.capacidad.application.configSwagger;

public final class ApiExamples {
    private ApiExamples() {}

    public static final String CAPACIDAD_DTO_JSON = """
    {
        "nombre": "Capacidad de Backend",
        "descripcion": "Desarrollo de servicios REST con Spring Boot",
        "tecnologias": [1, 2, 3]
    }
    """;

    public static final String CAPACIDAD_BOOTCAMP_DTO_JSON = """
    {
        "capacidadId": 1,
        "bootcampId": 2
    }
    """;

    public static final String DELETE_IDS_JSON = "[1, 2, 3]";
}
