package com.ejemplo.alojamiento.controller;

import com.ejemplo.alojamiento.model.Habitacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/habitacion")

public class HabitacionController {
    private static final Logger logger = LoggerFactory.getLogger(HabitacionController.class);
    private final List<Habitacion> habitaciones = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Habitacion>> getAll() {
        logger.info("Cargando todas las habitaciones");
        return new ResponseEntity<>(habitaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> getById(@PathVariable Long id) {
        Optional<Habitacion> habitacion = habitaciones.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
        if (habitacion.isPresent()) {
            logger.info("Habitación encontrada: {}", habitacion.get().getNumero());
            return new ResponseEntity<>(habitacion.get(), HttpStatus.OK);
        } else {
            logger.warn("Habitación con ID {} no encontrada", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Habitacion> create(@RequestBody Habitacion nueva) {
        if (nueva.getId() == null || nueva.getNumero() == null || nueva.getTipo() == null) {
            logger.error("Datos incompletos para crear una nueva habitación: {}", nueva);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        habitaciones.add(nueva);
        logger.info("Habitación añadida: {}", nueva);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habitacion> update(@PathVariable Long id, @RequestBody Habitacion actualizada){
        for (Habitacion h : habitaciones) {
            if (h.getId().equals(id)) {
                h.setNumero(actualizada.getNumero());
                h.setTipo(actualizada.getTipo());
                logger.info("Habitación actualizada: {}", h);
                return new ResponseEntity<>(h, HttpStatus.OK);
            }
        }
        logger.warn("No se encontró la habitación con ID {} para actualizar", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Habitacion> patch(@PathVariable Long id, @RequestBody Habitacion datos) {
        for (Habitacion h : habitaciones) {
            if (h.getId().equals(id)) {
                if (datos.getNumero() != null) h.setNumero(datos.getNumero());
                if (datos.getTipo() != null) h.setTipo(datos.getTipo());
                logger.info("Habitación modificada parcialmente: {}", h);
                return new ResponseEntity<>(h, HttpStatus.OK);
            }
        }
        logger.warn("No se encontró la habitación con ID {} para modificación parcial", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean eliminado = habitaciones.removeIf(h -> h.getId().equals(id));
        if (eliminado) {
            logger.info("Habitación con ID {} eliminada", id);
            return new ResponseEntity<>("Habitación eliminada", HttpStatus.OK);
        } else {
            logger.warn("No se encontró la habitación con ID {} para eliminar", id);
            return new ResponseEntity<>("No se encontró la habitación", HttpStatus.NOT_FOUND);
        }
    }
}
