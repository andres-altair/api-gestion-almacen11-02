package com.example.api_gestion_almacen.servicios;

import com.example.api_gestion_almacen.entidades.SectorEntidad;
import com.example.api_gestion_almacen.repositorios.SectorRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con los sectores.
 */
@Service
public class SectorServicio {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SectorServicio.class);
    
    @Autowired
    private SectorRepositorio sectorRepositorio;
    
    /**
     * Lista todos los sectores.
     *
     * @return Lista de todas las entidades de sector.
     */
    public List<SectorEntidad> listarTodos() {
        return sectorRepositorio.findAll();
    }
    
    /**
     * Lista todos los sectores que están disponibles.
     *
     * @return Lista de entidades de sector que están disponibles.
     */
    public List<SectorEntidad> listarDisponibles() {
        return sectorRepositorio.findByEstado(SectorEntidad.EstadoSector.DISPONIBLE);
    }
    
    /**
     * Busca un sector por su ID.
     *
     * @param id ID del sector que se desea buscar.
     * @return Un objeto Optional que contiene el sector si se encuentra, o vacío si no se encuentra.
     */
    public Optional<SectorEntidad> buscarPorId(Long id) {
        return sectorRepositorio.findById(id);
    }
    
    /**
     * Busca un sector por su nombre.
     *
     * @param nombre Nombre del sector que se desea buscar.
     * @return Un objeto Optional que contiene el sector si se encuentra, o vacío si no se encuentra.
     */
    public Optional<SectorEntidad> buscarPorNombre(String nombre) {
        LOGGER.debug("Buscando sector por nombre: {}", nombre);
        return sectorRepositorio.findByNombre(nombre);
    }
    
    /**
     * Actualiza el estado de un sector específico.
     *
     * @param id ID del sector cuyo estado se desea actualizar.
     * @param estado Nuevo estado del sector.
     * @return La entidad de sector actualizada.
     * @throws RuntimeException si el sector no se encuentra.
     */
    @Transactional
    public SectorEntidad actualizarEstado(Long id, SectorEntidad.EstadoSector estado) {
        SectorEntidad sector = sectorRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Sector no encontrado"));
        sector.setEstado(estado);
        return sectorRepositorio.save(sector);
    }
}