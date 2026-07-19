package com.onclass.capacidad.application.configSwagger;

public final class ApiConstants {
    private ApiConstants() {}

    // Rutas
    public static final String PATH_CAPACIDAD = "/capacidades";
    public static final String PATH_CAPACIDAD_BOOTCAMP = "/capacidad-bootcamps";
    public static final String PATH_BOOTCAMP_ID = "/{bootcampId}";
    public static final String PATH_CAPACIDADES = "/capacidades";
    public static final String PATH_CAPACIDAD_ID_BOOTCAMPS_COUNT = "/{capacidadId}/bootcamps/count";


    // Tipos de esquema
    public static final String SCHEMA_TYPE_STRING = "string";
    
    // Nombres de ejemplos
    public static final String EXAMPLE_CAPACIDAD_NAME = "Ejemplo Capacidad";
    public static final String EXAMPLE_CAPACIDAD_BOOTCAMP_NAME = "Ejemplo CapacidadBootcamp";
    public static final String EXAMPLE_DELETE_IDS_NAME = "Ejemplo eliminación por IDs";

    // Métodos de bean
    public static final String CREATE_CAPACIDAD_METHOD = "createCapacidad";
    public static final String GET_CAPACIDADES_METHOD = "getCapacidades";
    public static final String CREATE_CAPACIDAD_BOOTCAMPS_METHOD = "createCapacidadBootcamps";
    public static final String DELETE_CAPACIDADES_BY_BOOTCAMP_METHOD = "deleteCapacidadesByBootcamp";
    public static final String LIST_CAPACIDADES_BY_BOOTCAMP_METHOD = "listCapacidadesByBootcamp";
    public static final String COUNT_BOOTCAMPS_BY_CAPACIDAD_METHOD = "countBootcampsByCapacidadId";
    public static final String DELETE_CAPACIDADES_BY_IDS_METHOD = "deleteCapacidadesByIds";

    // Operaciones
    public static final String CREATE_CAPACIDAD_OPERATION_ID = "createCapacidad";
    public static final String CREATE_CAPACIDAD_SUMMARY = "Registrar una nueva capacidad";
    public static final String CREATE_CAPACIDAD_BOOTCAMP_OPERATION_ID = "createCapacidadBootcamp";
    public static final String CREATE_CAPACIDAD_BOOTCAMP_SUMMARY = "Registrar la relación capacidad - bootcamp";
    public static final String GET_CAPACIDADES_OPERATION_ID = "getCapacidades";
    public static final String GET_CAPACIDADES_SUMMARY = "Obtener el listado de capacidades";
    public static final String GET_CAPACIDADES_DESCRIPTION = "Obtiene todas las capacidades paginadas. Se pueden incluir parámetros opcionales de paginación y ordenamiento.";
    public static final String DELETE_CAPACIDAD_BOOTCAMP_OPERATION_ID = "deleteCapacidadesByBootcamp";
    public static final String DELETE_CAPACIDAD_BOOTCAMP_SUMMARY = "Eliminar capacidades asociadas a un bootcamp";
    public static final String DELETE_CAPACIDAD_BOOTCAMP_DESCRIPTION = "Elimina las capacidades asociadas a un bootcamp específico. " +
            "La operación es transaccional e ignora las capacidades compartidas con otros bootcamps.";

    public static final String COUNT_BOOTCAMPS_BY_CAPACIDAD_OPERATION_ID = "countBootcampsByCapacidadId";
    public static final String COUNT_BOOTCAMPS_BY_CAPACIDAD_SUMMARY = "Contar bootcamps asociados a una capacidad";
    public static final String COUNT_BOOTCAMPS_BY_CAPACIDAD_DESCRIPTION = "Retorna el número de bootcamps asociados a una capacidad específica.";

    public static final String DELETE_CAPACIDADES_BY_IDS_OPERATION_ID = "deleteCapacidadesByIds";
    public static final String DELETE_CAPACIDADES_BY_IDS_SUMMARY = "Eliminar capacidades por lista de IDs";
    public static final String DELETE_CAPACIDADES_BY_IDS_DESCRIPTION = "Elimina varias capacidades según una lista de IDs enviados en el cuerpo de la solicitud.";

    public static final String GET_CAPACIDADES_BY_BOOTCAMP_OPERATION_ID = "getCapacidadesByBootcamp";
    public static final String GET_CAPACIDADES_BY_BOOTCAMP_SUMMARY = "Obtiene todas las capacidades asociadas a un bootcamp";
    public static final String GET_CAPACIDADES_BY_BOOTCAMP_DESCRIPTION = "Retorna todas las capacidades que están asociadas a un bootcamp específico según su ID.";


    // Headers
    public static final String HEADER_X_MESSAGE_ID = "x-message-id";
    public static final String HEADER_X_MESSAGE_ID_DESC = "Identificador único del mensaje para trazabilidad";

    // Request Body
    public static final String REQUEST_BODY_DESCRIPTION = "Datos de la capacidad a registrar";
    public static final String REQUEST_BODY_CAPACIDAD_BOOTCAMP_DESCRIPTION = "Datos de la relación capacidad - bootcamp a registrar";
    public static final String REQUEST_BODY_DELETE_IDS_DESCRIPTION = "Lista de IDs de capacidades a eliminar";

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

    // Parámetros
    public static final String PARAM_BOOTCAMP_ID = "bootcampId";
    public static final String PARAM_BOOTCAMP_ID_DESC = "ID del bootcamp cuyas capacidades serán eliminadas";
    public static final String PARAM_BOOTCAMP_ID_DESC_SIMPLE = "ID del bootcamp";
    public static final String PARAM_CAPACIDAD_ID = "capacidadId";
    public static final String PARAM_CAPACIDAD_ID_DESC = "ID de la capacidad a consultar";

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
