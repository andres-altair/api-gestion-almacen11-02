package com.example.api_gestion_almacen.repositorios;

import com.example.api_gestion_almacen.entidades.AlquilerEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlquilerRepositorio extends JpaRepository<AlquilerEntidad, Long> {
    List<AlquilerEntidad> findByUsuarioId(Long usuarioId);
    List<AlquilerEntidad> findBySectorIdAndEstado(Long sectorId, AlquilerEntidad.EstadoAlquiler estado);
    List<AlquilerEntidad> findByFechaFinBefore(LocalDateTime fecha);
}