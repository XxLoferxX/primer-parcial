package com.ejemplo.alojamiento.Repository;
import com.ejemplo.alojamiento.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long>{
    Optional<Persona> findByGmail(String gmail);
}
