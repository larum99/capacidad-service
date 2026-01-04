package com.onclass.capacidad.infrastructure.adapters.util;

public final class ClientConstants {
    private ClientConstants() {}

    // URIs
    public static final String URI_CAPACIDAD_TECNOLOGIAS = "/capacidad-tecnologias";
    public static final String URI_CAPACIDAD_TECNOLOGIAS_BY_ID = "/capacidad-tecnologias/{id}/tecnologias";
    
    // Headers
    public static final String DEFAULT_MESSAGE_ID = "12345";
    
    // Configuration Properties
    public static final String TECNOLOGIA_SERVICE_URL_PROPERTY = "${services.tecnologia.url}";
}