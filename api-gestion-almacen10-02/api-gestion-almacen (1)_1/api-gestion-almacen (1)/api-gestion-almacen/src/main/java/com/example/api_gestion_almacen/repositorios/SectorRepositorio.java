package com.example.api_gestion_almacen.repositorios;

import com.example.api_gestion_almacen.entidades.SectorEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SectorRepositorio extends JpaRepository<SectorEntidad, Long> {
    List<SectorEntidad> findByEstado(SectorEntidad.EstadoSector estado);
    Optional<SectorEntidad> findByNombre(String nombre);
    List<SectorEntidad> findByMetrosCuadradosGreaterThanEqual(Integer metrosCuadrados);
    List<SectorEntidad> findByPrecioMensualLessThanEqual(BigDecimal precioMaximo);
}