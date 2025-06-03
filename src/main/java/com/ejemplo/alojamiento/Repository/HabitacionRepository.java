package com.ejemplo.alojamiento.Repository;
import com.ejemplo.alojamiento.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
}
