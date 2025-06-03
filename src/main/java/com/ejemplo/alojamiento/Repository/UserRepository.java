package com.ejemplo.alojamiento.Repository;
import com.ejemplo.alojamiento.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ejemplo.alojamiento.model.Cliente;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}