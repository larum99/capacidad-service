package com.onclass.capacidad.domain.enums;

public enum TechnicalMessage {

    // Errores genéricos
    INTERNAL_ERROR("500","Ha ocurrido un error interno, por favor intente nuevamente", ""),
    INVALID_REQUEST("400", "Solicitud incorrecta, por favor verifique los datos", ""),
    INVALID_PARAMETERS("400-1", "Parámetros inválidos, por favor verifique los datos", ""),

    // Mensajes específicos de Capacidad
    CAPACIDAD_ALREADY_EXISTS("400-2","La capacidad ya está registrada","nombre"),
    CAPACIDAD_NOMBRE_REQUIRED("400-3","El nombre de la capacidad es obligatorio","nombre"),
    CAPACIDAD_DESCRIPCION_REQUIRED("400-4","La descripción de la capacidad es obligatoria","descripcion"),
    CAPACIDAD_NOMBRE_TOO_LONG("400-5","El nombre de la capacidad supera los 50 caracteres","nombre"),
    CAPACIDAD_DESCRIPCION_TOO_LONG("400-6","La descripción de la capacidad supera los 90 caracteres","descripcion"),
    CAPACIDAD_MIN_TECNOLOGIAS("400-7","La capacidad debe tener al menos 3 tecnologías asociadas","tecnologias"),
    CAPACIDAD_MAX_TECNOLOGIAS("400-8","La capacidad no puede tener más de 20 tecnologías","tecnologias"),
    CAPACIDAD_TECNOLOGIAS_DUPLICADAS("400-9","La capacidad no puede tener tecnologías repetidas","tecnologias"),
    CAPACIDAD_TECNOLOGIAS_NO_EXISTEN("400-10","Una o más tecnologías asociadas no existen","tecnologias"),
    CAPACIDAD_CREATED("201", "Capacidad creada exitosamente", "");


    private final String code;
    private final String description;
    private final String param;

    TechnicalMessage(String code, String description, String param) {
        this.code = code;
        this.description = description;
        this.param = param;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getParam() {
        return param;
    }
}
