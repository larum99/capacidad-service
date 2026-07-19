package com.onclass.capacidad.application.configSwagger;

public final class ApiConstants {
    private ApiConstants() {}

    // Rutas
    public static final String PATH_CAPACIDAD = "/capacidades";
    public static final String PATH_CAPACIDAD_BOOTCAMP = "/capacidad-bootcamps";


    // Operaciones
    public static final String CREATE_CAPACIDAD_OPERATION_ID = "createCapacidad";
    public static final String CREATE_CAPACIDAD_SUMMARY = "Registrar una nueva capacidad";
    public static final String CREATE_CAPACIDAD_BOOTCAMP_OPERATION_ID = "createCapacidadBootcamp";
    public static final String CREATE_CAPACIDAD_BOOTCAMP_SUMMARY = "Registrar la relación capacidad - bootcamp";
    public static final String GET_CAPACIDADES_OPERATION_ID = "getCapacidades";
    public static final String GET_CAPACIDADES_SUMMARY = "Obtener el listado de capacidades";
    public static final String DELETE_CAPACIDAD_BOOTCAMP_OPERATION_ID = "deleteCapacidadesByBootcamp";
    public static final String DELETE_CAPACIDAD_BOOTCAMP_SUMMARY = "Eliminar capacidades asociadas a un bootcamp";

    public static final String COUNT_BOOTCAMPS_BY_CAPACIDAD_OPERATION_ID = "countBootcampsByCapacidadId";
    public static final String COUNT_BOOTCAMPS_BY_CAPACIDAD_SUMMARY = "Contar bootcamps asociados a una capacidad";

    public static final String DELETE_CAPACIDADES_BY_IDS_OPERATION_ID = "deleteCapacidadesByIds";
    public static final String DELETE_CAPACIDADES_BY_IDS_SUMMARY = "Eliminar capacidades por lista de IDs";


    // Headers
    public static final String HEADER_X_MESSAGE_ID = "x-message-id";
    public static final String HEADER_X_MESSAGE_ID_DESC = "Identificador único del mensaje para trazabilidad";

    // Request Body
    public static final String REQUEST_BODY_DESCRIPTION = "Datos de la capacidad a registrar";
    public static final String REQUEST_BODY_CAPACIDAD_BOOTCAMP_DESCRIPTION = "Datos de la relación capacidad - bootcamp a registrar";

    // Códigos HTTP
    public static final String HTTP_OK = "200";
    public static final String HTTP_CREATED = "201";
    public static final String HTTP_BAD_REQUEST = "400";
    public static final String HTTP_INTERNAL_ERROR = "500";
    public static final String HTTP_NO_CONTENT = "204";
    public static final String HTTP_NOT_FOUND = "404";

    // Descripciones de respuesta
    public static final String RESPONSE_201 = "Capacidad creada exitosamente";
    public static final String RESPONSE_400 = "Solicitud inválida";
    public static final String RESPONSE_500 = "Error interno del servidor";
    public static final String RESPONSE_CAPACIDAD_BOOTCAMP_201 = "Relación capacidad - bootcamp creada exitosamente";
    public static final String RESPONSE_CAPACIDAD_BOOTCAMP_400 = "Solicitud inválida para la relación capacidad - bootcamp";
    public static final String RESPONSE_CAPACIDAD_BOOTCAMP_500 = "Error interno del servidor al crear la relación capacidad - bootcamp";
    public static final String RESPONSE_200 = "Listado de capacidades devuelto correctamente";
    public static final String RESPONSE_204 = "Capacidades eliminadas exitosamente.";
    public static final String RESPONSE_404 = "No se encontraron capacidades asociadas al bootcamp indicado.";

    // Parámetros de consulta
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_PAGE_DESC = "Número de página (opcional, default 0)";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_SIZE_DESC = "Cantidad de elementos por página (opcional, default 10)";
    public static final String PARAM_SORT_BY = "sortBy";
    public static final String PARAM_SORT_BY_DESC = "Campo por el cual ordenar (opcional)";
    public static final String PARAM_SORT_ORDER = "sortOrder";
    public static final String PARAM_SORT_ORDER_DESC = "Dirección del orden: asc o desc (opcional)";

}
