package com.ejemplo.alojamiento.model;


public class mensage {
    private String contenido;
    private String tipo;

    public mensage() {}

    public mensage(String contenido, String tipo) {
        this.contenido = contenido;
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
