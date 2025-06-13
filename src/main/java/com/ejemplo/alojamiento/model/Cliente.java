package com.ejemplo.alojamiento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")

public class Cliente {
    @Id
    public Long id;
    private String nombre;
    private String gmail;
    private String telefono;

    /**    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<persona> books;
     **/
    public Cliente() {
    }

    public Cliente(Long id, String nombre, String gmail, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.gmail = gmail;
        this.telefono = telefono;
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

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}

