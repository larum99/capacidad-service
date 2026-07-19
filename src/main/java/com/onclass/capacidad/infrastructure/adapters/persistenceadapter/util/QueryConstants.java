package com.onclass.capacidad.infrastructure.adapters.persistenceadapter.util;

public final class QueryConstants {
    private QueryConstants() {}

    // CapacidadBootcampRepository queries
    public static final String FIND_CAPACIDADES_BY_BOOTCAMP_ID = """
        SELECT c.id AS id, c.nombre AS nombre, c.descripcion AS descripcion
        FROM capacidad_bootcamp cb
        JOIN capacidades c ON cb.id_capacidad = c.id
        WHERE cb.id_bootcamp = :bootcampId
        """;
    
    public static final String FIND_CAPACIDAD_IDS_BY_BOOTCAMP_ID = 
        "SELECT id_capacidad FROM capacidad_bootcamp WHERE id_bootcamp = :bootcampId";
    
    public static final String DELETE_ALL_BY_BOOTCAMP_ID = 
        "DELETE FROM capacidad_bootcamp WHERE id_bootcamp = :bootcampId";

    // CapacidadRepository queries
    public static final String DELETE_BY_IDS = 
        "DELETE FROM capacidades WHERE id IN (:capacidadIds)";
    
    public static final String COUNT_BY_ID_IN = 
        "SELECT COUNT(*) FROM capacidades WHERE id IN (:capacidadIds)";

    // CustomCapacidadRepositoryImpl queries
    public static final String SELECT_CAPACIDADES_BASE = """
        SELECT c.id, c.nombre, c.descripcion
        FROM capacidades c
        """;
    
    public static final String COUNT_CAPACIDADES = 
        "SELECT COUNT(*) AS total FROM capacidades c";
    
    public static final String ORDER_BY_NOMBRE = " ORDER BY c.nombre ";
    public static final String LIMIT_OFFSET = " LIMIT %d OFFSET %d";

    // Sort orders
    public static final String SORT_ORDER_ASC = "ASC";
    public static final String SORT_ORDER_DESC = "DESC";
    public static final String SORT_BY_NOMBRE = "nombre";
    
    // Column names for mapping
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL = "total";
}