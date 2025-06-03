package com.ejemplo.alojamiento.controller;


import  com.ejemplo.alojamiento.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);
    private final List<Persona> personas = new ArrayList<>();



    @GetMapping
    public ResponseEntity<List<Persona>> getAll() {
        logger.info("Cargando Personas");
        return new ResponseEntity<>(personas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getById(@PathVariable Long id) {
        Optional<Persona> persona = personas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (persona.isPresent()){
            logger.info("persona encontrada: {}", persona.get().getNombre());
            return new ResponseEntity<>(persona.get(), HttpStatus.OK);
        } else {
            logger.warn("la persona con el id {} no fue encontrada", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Persona> create(@RequestBody Persona nueva){
        if (nueva.getId()==null || nueva.getNombre()==null || nueva.getgmail()==null){
            logger.error("Datos incompletos para la correcta realizacion de una nueva persona: {}",nueva);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        personas.add(nueva);
        logger.info("La persona fue añadida correctamente: {}",nueva);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);


    }

    @PutMapping("/{id}")
    public ResponseEntity<Persona> update(@PathVariable Long id, @RequestBody Persona actualizada) {
        for (Persona p : personas) {
            if (p.getId().equals(id)) {
                p.setNombre(actualizada.getNombre());
                p.setgmail(actualizada.getgmail());
                logger.info("Persona actualizada: {}", p);
                return new ResponseEntity<>(p, HttpStatus.OK);
            }
        }
        logger.warn("No se pudo encontrar a la persona con el id: {} para la actualizacion", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Persona> patch(@PathVariable Long id, @RequestBody Persona datos) {
        for (Persona p : personas) {
            if (p.getId().equals(id)) {
                if (datos.getNombre() != null) p.setNombre(datos.getNombre());
                if (datos.getgmail() != null) p.setgmail(datos.getgmail());
                logger.info("Persona modificada parcialmente: {}", p);
                return new ResponseEntity<>(p, HttpStatus.OK);
            }
        }
        logger.warn("No se pudo encontrar a la persona con el id: {} para la actualizacion parcial", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean eliminado = personas.removeIf(p -> p.getId().equals(id));
        if (eliminado){
            logger.info("la persona con el id {} fue correctamente eliminado", id);
            return new ResponseEntity<>("persona eliminada", HttpStatus.OK);
        }else {
            logger.warn("No se pudo encontrar a la persona con el id: {} para ser eliminado", id);
            return new ResponseEntity<>("persona no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
