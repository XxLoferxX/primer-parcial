package com.ejemplo.alojamiento.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ejemplo.alojamiento.model.Habitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    Optional<Habitacion> findByNumero(int numero);
}
