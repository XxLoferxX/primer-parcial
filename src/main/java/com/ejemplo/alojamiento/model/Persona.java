package com.ejemplo.alojamiento.model;

import jakarta.persistence.*;

@Entity

public class Persona {
    @Id
    private Long id;

    private String nombre;
    private String gmail;

    public Persona() {
    }

    public Persona(Long id, String nombre, String gmail) {
        this.id = id;
        this.nombre = nombre;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getgmail() {
        return gmail;
    }

    public void setgmail(String gmail) {
        this.gmail = gmail;
    }
}