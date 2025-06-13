package com.ejemplo.alojamiento.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.ejemplo.alojamiento.model.Persona;
import com.ejemplo.alojamiento.model.mensage;
import com.ejemplo.alojamiento.Repository.PersonaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/personas")
@Tag(name = "Persona", description = "Operaciones relacionadas con personas")
public class PersonaController {

    private static final Logger logger = LoggerFactory.getLogger(PersonaController.class);

    private final PersonaRepository personaRepository;

    public PersonaController(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Operation(summary = "Obtener todas las personas")
    @ApiResponse(responseCode = "200", description = "Personas obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        List<Persona> personas = personaRepository.findAll();
        return ResponseEntity.ok(personas);
    }

    @Operation(summary = "Obtener una persona por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona encontrada"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersona(@PathVariable Long id) {
        logger.info("Buscando persona con ID {}", id);
        Optional<Persona> persona = personaRepository.findById(id);
        return persona.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Crear una nueva persona")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Persona creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<?> createPersona(@RequestBody Persona persona) {
        try {
            // Verificamos si ya existe una persona con ese gmail
            Optional<Persona> existente = personaRepository.findByGmail(persona.getgmail());
            if (existente.isPresent()) {
                logger.warn("Intento de crear persona duplicada con gmail: {}", persona.getgmail());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new mensage("Ya existe una persona registrada con ese gmail", "error"));
            }

            Persona saved = personaRepository.save(persona);
            logger.info("Persona creada con ID {}", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            logger.error("Error al crear persona", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Eliminar una persona")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persona eliminada"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<mensage> deletePersona(@PathVariable Long id) {
        if (personaRepository.existsById(id)) {
            personaRepository.deleteById(id);
            return ResponseEntity.ok(new mensage("La persona fue eliminada correctamente", "success"));
        } else {
            logger.warn("No se encontró persona con ID {}", id);
            return ResponseEntity.ok(new mensage("La persona no fue encontrada", "not_found"));
        }
    }

@Operation(summary = "Actualizar completamente una persona")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persona actualizada"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
})
@PutMapping("/{id}")
public ResponseEntity<Persona> updatePersona(@PathVariable Long id, @RequestBody Persona updatedPersona) {
    Optional<Persona> optional = personaRepository.findById(id);
    if (optional.isPresent()) {
        Persona persona = optional.get();
        persona.setNombre(updatedPersona.getNombre());
        persona.setgmail(updatedPersona.getgmail());
        Persona saved = personaRepository.save(persona);
        logger.info("Persona actualizada con ID {}", id);
        return ResponseEntity.ok(saved);
    } else {
        logger.warn("No se encontró persona con ID {} para actualizar", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

@Operation(summary = "Actualizar parcialmente una persona")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persona actualizada parcialmente"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
})
@PatchMapping("/{id}")
public ResponseEntity<Persona> patchPersona(@PathVariable Long id, @RequestBody Persona partialPersona) {
    Optional<Persona> optional = personaRepository.findById(id);
    if (optional.isPresent()) {
        Persona persona = optional.get();
        if (partialPersona.getNombre() != null) {
            persona.setNombre(partialPersona.getNombre());
        }
        if (partialPersona.getgmail() != null) {
            persona.setgmail(partialPersona.getgmail());
        }
        Persona saved = personaRepository.save(persona);
        logger.info("Persona parcialmente actualizada con ID {}", id);
        return ResponseEntity.ok(saved);
    } else {
        logger.warn("No se encontró persona con ID {} para patch", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
}