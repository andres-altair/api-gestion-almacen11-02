package com.example.api_gestion_almacen.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.api_gestion_almacen.entidades.RolEntidad;

/**
 * Interfaz RolRepositorio que extiende JpaRepository.
 * Esta interfaz proporciona métodos para realizar operaciones CRUD
 * sobre la entidad RolEntidad en la base de datos.
 * 
 * @author andres
 */
@Repository // Indica que esta interfaz es un componente de acceso a datos
public interface RolRepositorio extends JpaRepository<RolEntidad, Long> {}