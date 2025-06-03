package com.ejemplo.alojamiento.Repository;
import com.ejemplo.alojamiento.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
