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

/**
 * Servicio para gestionar las operaciones relacionadas con los alquileres.
 */
@Service
public class AlquilerServicio {
    
    @Autowired
    private AlquilerRepositorio alquilerRepositorio;
    
    @Autowired
    private SectorServicio sectorServicio;
    
    /**
     * Crea un nuevo alquiler.
     *
     * @param sectorId ID del sector que se va a alquilar.
     * @param usuarioId ID del usuario que realiza el alquiler.
     * @param ordenId ID de la orden asociada al alquiler.
     * @param montoPagado Monto pagado por el alquiler.
     * @param fechaInicio Fecha y hora de inicio del alquiler.
     * @param fechaFin Fecha y hora de finalización del alquiler.
     * @return La entidad de alquiler creada.
     * @throws RuntimeException si el sector no está disponible o no se encuentra.
     */
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
    
    /**
     * Lista todos los alquileres de un usuario específico.
     *
     * @param usuarioId ID del usuario cuyos alquileres se desean listar.
     * @return Lista de entidades de alquiler asociadas al usuario.
     */
    public List<AlquilerEntidad> listarAlquileresUsuario(Long usuarioId) {
        return alquilerRepositorio.findByUsuarioId(usuarioId);
    }
    
    /**
     * Finaliza un alquiler específico.
     *
     * @param alquilerId ID del alquiler que se desea finalizar.
     * @throws RuntimeException si el alquiler no se encuentra.
     */
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