package com.example.api_gestion_almacen.servicios;

import com.example.api_gestion_almacen.entidades.AlquilerEntidad;
import com.example.api_gestion_almacen.entidades.SectorEntidad;
import com.example.api_gestion_almacen.repositorios.AlquilerRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlquilerServicio {
    
    @Autowired
    private AlquilerRepositorio alquilerRepositorio;
    
    @Autowired
    private SectorServicio sectorServicio;
    
    @Transactional
    public AlquilerEntidad crearAlquiler(Long sectorId, Long usuarioId, String ordenId, 
                                        BigDecimal montoPagado, LocalDateTime fechaInicio, 
                                        LocalDateTime fechaFin) {
        // Verificar y reservar el sector
        SectorEntidad sector = sectorServicio.buscarPorId(sectorId)
            .orElseThrow(() -> new RuntimeException("Sector no encontrado"));
            
        if (sector.getEstado() != SectorEntidad.EstadoSector.DISPONIBLE) {
            throw new RuntimeException("El sector no está disponible");
        }
        
        // Crear el alquiler
        AlquilerEntidad alquiler = new AlquilerEntidad();
        alquiler.setSector(sector);
        alquiler.setUsuarioId(usuarioId);
        alquiler.setOrdenId(ordenId);
        alquiler.setMontoPagado(montoPagado);
        alquiler.setFechaInicio(fechaInicio);
        alquiler.setFechaFin(fechaFin);
        alquiler.setEstado(AlquilerEntidad.EstadoAlquiler.ACTIVO);
        
        // Marcar el sector como ocupado
        sectorServicio.actualizarEstado(sectorId, SectorEntidad.EstadoSector.OCUPADO);
        
        return alquilerRepositorio.save(alquiler);
    }
    
    public List<AlquilerEntidad> listarAlquileresUsuario(Long usuarioId) {
        return alquilerRepositorio.findByUsuarioId(usuarioId);
    }
    
    @Transactional
    public void finalizarAlquiler(Long alquilerId) {
        AlquilerEntidad alquiler = alquilerRepositorio.findById(alquilerId)
            .orElseThrow(() -> new RuntimeException("Alquiler no encontrado"));
            
        alquiler.setEstado(AlquilerEntidad.EstadoAlquiler.FINALIZADO);
        sectorServicio.actualizarEstado(alquiler.getSector().getId(), 
                                      SectorEntidad.EstadoSector.DISPONIBLE);
        
        alquilerRepositorio.save(alquiler);
    }
}