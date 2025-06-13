package com.ejemplo.alojamiento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "habitacion", uniqueConstraints = {
        @UniqueConstraint(columnNames = "numero")
})
public class Habitacion {

    @Id
    private Long id;

    private int numero;
    private String tipo;
    private int capacidad;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}