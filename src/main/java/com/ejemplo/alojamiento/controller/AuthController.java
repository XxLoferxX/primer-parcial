package com.ejemplo.alojamiento.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.OneToMany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.alojamiento.Repository.UserRepository;
import com.ejemplo.alojamiento.model.User;
import com.ejemplo.alojamiento.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ejemplo.alojamiento.config.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;


import java.util.Optional;

@RestController
@RequestMapping("/auth")

@Tag(name = "User", description = "Operations related to users")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(userRequest.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Verificamos contraseña con BCrypt
            if (passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok().body(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no existe");
        }
    }
    @Autowired
    private TokenBlacklist tokenBlacklist;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.add(token); // Aquí se añade a la lista negra
        }
        return ResponseEntity.ok().body("Sesión cerrada correctamente");
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El nombre de usuario '" + user.getUsername() + "' ya está registrado.");
        }

        try {
            // Hashear la contraseña antes de guardar
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar el usuario.");
        }
    }
}