package com.onclass.capacidad.domain.utils;

public class TecnologiaSummary {

    private Long id;
    private String nombre;

    public TecnologiaSummary(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
