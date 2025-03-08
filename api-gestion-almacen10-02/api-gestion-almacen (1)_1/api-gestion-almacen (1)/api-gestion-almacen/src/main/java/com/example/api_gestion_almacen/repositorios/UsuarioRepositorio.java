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
     /**
     * Encuentra un usuario por su correo electrónico.
     *
     * @param correoElectronico El correo electrónico del usuario que se desea encontrar.
     * @return Un objeto Optional que contiene el usuario si se encuentra, o vacío si no se encuentra.
     */
    Optional<UsuarioEntidad> findByCorreoElectronico(String correoElectronico);
}


