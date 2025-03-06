package com.example.api_gestion_almacen.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.api_gestion_almacen.entidades.UsuarioEntidad;

/**
 * Interfaz UsuarioRepositorio que extiende JpaRepository.
 * Esta interfaz proporciona métodos para realizar operaciones CRUD
 * sobre la entidad UsuarioRepositorio en la base de datos.
 * 
 * @author andres
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<UsuarioEntidad, Long> {
    Optional<UsuarioEntidad> findByCorreoElectronico(String correoElectronico);
}



