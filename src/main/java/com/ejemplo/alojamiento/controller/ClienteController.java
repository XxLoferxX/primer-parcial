package com.ejemplo.alojamiento.controller;

import com.ejemplo.alojamiento.model.Cliente;
import com.ejemplo.alojamiento.Repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        logger.info("Cargando todos los clientes");
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            logger.info("Cliente encontrado: {}", cliente.get());
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            logger.warn("Cliente con ID {} no encontrado", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cliente nuevo) {
        if (nuevo.getNombre() == null || nuevo.getGmail() == null || nuevo.getTelefono() == null) {
            logger.error("Datos incompletos para crear cliente: {}", nuevo);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        boolean existe = clienteRepository.existsByGmail(nuevo.getGmail());
        if (existe) {
            logger.warn("Cliente con correo {} ya existe", nuevo.getGmail());
            return new ResponseEntity<>("Cliente ya registrado con ese correo", HttpStatus.CONFLICT);
        }

        Cliente saved = clienteRepository.save(nuevo);
        logger.info("Cliente guardado en base: {}", saved);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente actualizado) {
        Optional<Cliente> optional = clienteRepository.findById(id);
        if (optional.isPresent()) {
            Cliente cliente = optional.get();
            cliente.setNombre(actualizado.getNombre());
            cliente.setGmail(actualizado.getGmail());
            cliente.setTelefono(actualizado.getTelefono());
            Cliente saved = clienteRepository.save(cliente);
            logger.info("Cliente actualizado: {}", saved);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }
        logger.warn("Cliente no encontrado con ID {}", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> patch(@PathVariable Long id, @RequestBody Cliente datos) {
        Optional<Cliente> optional = clienteRepository.findById(id);
        if (optional.isPresent()) {
            Cliente cliente = optional.get();
            if (datos.getNombre() != null) cliente.setNombre(datos.getNombre());
            if (datos.getGmail() != null) cliente.setGmail(datos.getGmail());
            if (datos.getTelefono() != null) cliente.setTelefono(datos.getTelefono());
            Cliente saved = clienteRepository.save(cliente);
            logger.info("Cliente modificado parcialmente: {}", saved);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }
        logger.warn("Cliente no encontrado con ID {} para patch", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            logger.info("Cliente eliminado con ID {}", id);
            return new ResponseEntity<>("Cliente eliminado", HttpStatus.OK);
        }
        logger.warn("Cliente no encontrado con ID {} para eliminar", id);
        return new ResponseEntity<>("No se encontró el cliente", HttpStatus.NOT_FOUND);
    }
}

