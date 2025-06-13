package com.ejemplo.alojamiento.Repository;

import com.ejemplo.alojamiento.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByGmail(String gmail);
}
