package com.onclass.capacidad.infrastructure.entrypoints.util;

public final class HandlerConstants {
    private HandlerConstants() {}

    // Path variables
    public static final String PATH_VARIABLE_BOOTCAMP_ID = "bootcampId";
    public static final String PATH_VARIABLE_CAPACIDAD_ID = "capacidadId";
    
    // Log messages
    public static final String LOG_ERROR_LISTAR_CAPACIDADES = "Error al listar capacidades para bootcamp {}";
    public static final String LOG_INFO_CAPACIDADES_ELIMINADAS = "Capacidades eliminadas para bootcamp {}";
    public static final String LOG_ERROR_ELIMINAR_CAPACIDADES = "Error al eliminar capacidades del bootcamp {}";
    public static final String LOG_ERROR_CONTAR_BOOTCAMPS = "Error al contar bootcamps para capacidad {}: {}";
}