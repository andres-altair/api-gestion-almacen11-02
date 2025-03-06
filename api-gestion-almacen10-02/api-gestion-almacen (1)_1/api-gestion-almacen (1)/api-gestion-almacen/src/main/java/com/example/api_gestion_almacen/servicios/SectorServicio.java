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

@Service
public class SectorServicio {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SectorServicio.class);
    
    @Autowired
    private SectorRepositorio sectorRepositorio;
    
    public List<SectorEntidad> listarTodos() {
        return sectorRepositorio.findAll();
    }
    
    public List<SectorEntidad> listarDisponibles() {
        return sectorRepositorio.findByEstado(SectorEntidad.EstadoSector.DISPONIBLE);
    }
    
    public Optional<SectorEntidad> buscarPorId(Long id) {
        return sectorRepositorio.findById(id);
    }
    
    public Optional<SectorEntidad> buscarPorNombre(String nombre) {
        LOGGER.debug("Buscando sector por nombre: {}", nombre);
        return sectorRepositorio.findByNombre(nombre);
    }
    
    @Transactional
    public SectorEntidad actualizarEstado(Long id, SectorEntidad.EstadoSector estado) {
        SectorEntidad sector = sectorRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Sector no encontrado"));
        sector.setEstado(estado);
        return sectorRepositorio.save(sector);
    }
}