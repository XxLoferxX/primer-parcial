package com.ejemplo.alojamiento.controller;

import com.ejemplo.alojamiento.model.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final List<Cliente> clientes = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        logger.info("Cargando todos los clientes");
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        Optional<Cliente> cliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
        if (cliente.isPresent()) {
            logger.info("Cliente encontrado: {}", cliente.get());
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            logger.warn("Cliente con ID {} no encontrado", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> create(@RequestBody Cliente nuevo) {
        if (nuevo.getId() == null || nuevo.getNombre() == null || nuevo.getGmail() == null || nuevo.getTelefono() == null) {
            logger.error("los datos estan incompletos para la creacion de un cliente: {}", nuevo);
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        clientes.add(nuevo);
        logger.info("Cliente añadido: {}", nuevo);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente actualizado) {
        for (Cliente c : clientes) {
            if (c.getId().equals(id)) {
                c.setNombre(actualizado.getNombre());
                c.setGmail(actualizado.getGmail());
                c.setTelefono(actualizado.getTelefono());
                logger.info("Cliente actualizado: {}", c);
                return new ResponseEntity<>(c, HttpStatus.OK);
            }
        }
        logger.warn("No se encontró el cliente con ID {} para actualizar", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> patch(@PathVariable Long id, @RequestBody Cliente datos) {
        for (Cliente c : clientes) {
            if (c.getId().equals(id)) {
                if (datos.getNombre() != null) c.setNombre(datos.getNombre());
                if (datos.getGmail() != null) c.setGmail(datos.getGmail());
                if (datos.getTelefono() != null) c.setTelefono(datos.getTelefono());
                logger.info("Cliente modificado parcialmente: {}", c);
                return new ResponseEntity<>(c, HttpStatus.OK);
            }
        }
        logger.warn("No se encontró el cliente con ID {} para modificación parcial", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean eliminado = clientes.removeIf(c -> c.getId().equals(id));
        if (eliminado) {
            logger.info("Cliente con ID {} eliminado", id);
            return new ResponseEntity<>("Cliente eliminado", HttpStatus.OK);
        } else {
            logger.warn("No se encontró el cliente con ID {} para eliminar", id);
            return new ResponseEntity<>("No se encontró el cliente", HttpStatus.NOT_FOUND);
        }
    }
}
