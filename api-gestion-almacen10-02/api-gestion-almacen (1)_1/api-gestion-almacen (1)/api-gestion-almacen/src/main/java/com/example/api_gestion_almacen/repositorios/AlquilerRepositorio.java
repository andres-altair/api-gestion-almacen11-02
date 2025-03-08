package com.example.api_gestion_almacen.repositorios;

import com.example.api_gestion_almacen.entidades.AlquilerEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar las operaciones de acceso a datos relacionadas con los alquileres.
 */
@Repository
public interface AlquilerRepositorio extends JpaRepository<AlquilerEntidad, Long> {

    /**
     * Encuentra todos los alquileres asociados a un usuario específico.
     *
     * @param usuarioId ID del usuario cuyos alquileres se desean encontrar.
     * @return Lista de entidades de alquiler asociadas al usuario.
     */
    List<AlquilerEntidad> findByUsuarioId(Long usuarioId);

    /**
     * Encuentra todos los alquileres asociados a un sector específico y con un estado determinado.
     *
     * @param sectorId ID del sector cuyos alquileres se desean encontrar.
     * @param estado Estado de los alquileres que se desean encontrar.
     * @return Lista de entidades de alquiler asociadas al sector y con el estado especificado.
     */
    List<AlquilerEntidad> findBySectorIdAndEstado(Long sectorId, AlquilerEntidad.EstadoAlquiler estado);

    /**
     * Encuentra todos los alquileres cuya fecha de finalización es anterior a la fecha especificada.
     *
     * @param fecha Fecha límite para la búsqueda de alquileres.
     * @return Lista de entidades de alquiler cuya fecha de finalización es anterior a la fecha especificada.
     */
    List<AlquilerEntidad> findByFechaFinBefore(LocalDateTime fecha);
}