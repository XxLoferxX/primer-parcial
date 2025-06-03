package com.ejemplo.alojamiento.controller;

import com.ejemplo.alojamiento.Repository.UserRepository;
import com.ejemplo.alojamiento.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.alojamiento.util.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "User", description = "Operations related to users")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(userRequest.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Aquí podrías verificar la contraseña si la tienes en tu modelo
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok().body(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no existe o credenciales inválidas");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("Sesión cerrada correctamente");
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User savedUser = userRepository.save(user);
            logger.info("User agregado: {}", savedUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            logger.error("Error al registrar usuario", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}