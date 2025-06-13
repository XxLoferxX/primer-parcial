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

import com.ejemplo.alojamiento.model.Habitacion;
import com.ejemplo.alojamiento.model.mensage;
import com.ejemplo.alojamiento.Repository.HabitacionRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/habitaciones")
@Tag(name = "Habitacion", description = "Operaciones relacionadas con habitaciones")
public class HabitacionController {

    private static final Logger logger = LoggerFactory.getLogger(HabitacionController.class);

    private final HabitacionRepository habitacionRepository;

    public HabitacionController(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    @Operation(summary = "Obtener todas las habitaciones")
    @ApiResponse(responseCode = "200", description = "Habitaciones obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<Habitacion>> getAllHabitaciones() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();
        return ResponseEntity.ok(habitaciones);
    }

    @Operation(summary = "Obtener una habitación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación encontrada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> getHabitacion(@PathVariable Long id) {
        logger.info("Buscando habitación con ID {}", id);
        Optional<Habitacion> habitacion = habitacionRepository.findById(id);
        return habitacion.map(value -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Crear una nueva habitación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<?> createHabitacion(@RequestBody Habitacion habitacion) {
        try {
            // Primero chequea si ya existe una habitación con ese número
            Optional<Habitacion> existing = habitacionRepository.findByNumero(habitacion.getNumero());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new mensage("Número de habitación ya existe", "error"));
            }
            Habitacion saved = habitacionRepository.save(habitacion);
            logger.info("Habitación creada con ID {}", saved.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error al crear habitación", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @Operation(summary = "Eliminar una habitación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación eliminada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<mensage> deleteHabitacion(@PathVariable Long id) {
        if (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
            logger.info("Habitación eliminada con ID {}", id);
            return ResponseEntity.ok(new mensage("Habitación eliminada correctamente", "success"));
        } else {
            logger.warn("No se encontró habitación con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new mensage("Habitación no encontrada", "error"));
        }
    }

    @Operation(summary = "Actualizar completamente una habitación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Habitacion> updateHabitacion(@PathVariable Long id, @RequestBody Habitacion updatedHabitacion) {
        Optional<Habitacion> optional = habitacionRepository.findById(id);
        if (optional.isPresent()) {
            Habitacion habitacion = optional.get();
            habitacion.setNumero(updatedHabitacion.getNumero());
            habitacion.setCapacidad(updatedHabitacion.getCapacidad());
            Habitacion saved = habitacionRepository.save(habitacion);
            logger.info("Habitación actualizada con ID {}", id);
            return ResponseEntity.ok(saved);
        } else {
            logger.warn("No se encontró habitación con ID {} para actualizar", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Actualizar parcialmente una habitación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada parcialmente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Habitacion> patchHabitacion(@PathVariable Long id, @RequestBody Habitacion partialHabitacion) {
        Optional<Habitacion> optional = habitacionRepository.findById(id);
        if (optional.isPresent()) {
            Habitacion habitacion = optional.get();

            if (partialHabitacion.getNumero() != -1) {
                habitacion.setNumero(partialHabitacion.getNumero());
            }
            if (partialHabitacion.getCapacidad() != -1) {
                habitacion.setCapacidad(partialHabitacion.getCapacidad());
            }

            Habitacion saved = habitacionRepository.save(habitacion);
            logger.info("Habitación parcialmente actualizada con ID {}", id);
            return ResponseEntity.ok(saved);
        } else {
            logger.warn("No se encontró habitación con ID {} para patch", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
