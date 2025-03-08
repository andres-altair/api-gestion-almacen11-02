package com.example.api_gestion_almacen.repositorios;

import com.example.api_gestion_almacen.entidades.SectorEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar las operaciones de acceso a datos relacionadas con los sectores.
 */
@Repository
public interface SectorRepositorio extends JpaRepository<SectorEntidad, Long> {

    /**
     * Encuentra todos los sectores con un estado específico.
     *
     * @param estado Estado de los sectores que se desean encontrar.
     * @return Lista de entidades de sector que coinciden con el estado especificado.
     */
    List<SectorEntidad> findByEstado(SectorEntidad.EstadoSector estado);

    /**
     * Encuentra un sector por su nombre.
     *
     * @param nombre Nombre del sector que se desea encontrar.
     * @return Un objeto Optional que contiene el sector si se encuentra, o vacío si no se encuentra.
     */
    Optional<SectorEntidad> findByNombre(String nombre);

    /**
     * Encuentra todos los sectores que tienen una cantidad de metros cuadrados mayor o igual a la cantidad especificada.
     *
     * @param metrosCuadrados Cantidad mínima de metros cuadrados que deben tener los sectores.
     * @return Lista de entidades de sector que cumplen con el criterio de metros cuadrados.
     */
    List<SectorEntidad> findByMetrosCuadradosGreaterThanEqual(Integer metrosCuadrados);

    /**
     * Encuentra todos los sectores cuyo precio mensual es menor o igual al precio máximo especificado.
     *
     * @param precioMaximo Precio máximo que deben tener los sectores.
     * @return Lista de entidades de sector que cumplen con el criterio de precio mensual.
     */
    List<SectorEntidad> findByPrecioMensualLessThanEqual(BigDecimal precioMaximo);
}