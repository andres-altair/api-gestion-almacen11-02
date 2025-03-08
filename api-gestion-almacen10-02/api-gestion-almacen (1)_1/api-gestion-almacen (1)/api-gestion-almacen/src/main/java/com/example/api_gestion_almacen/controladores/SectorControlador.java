package com.example.api_gestion_almacen.controladores;

import com.example.api_gestion_almacen.dtos.SectorDto;
import com.example.api_gestion_almacen.entidades.SectorEntidad;
import com.example.api_gestion_almacen.servicios.SectorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los sectores.
 */
@RestController
@RequestMapping("/api/sectores")
public class SectorControlador {
    private static final Logger LOGGER = LoggerFactory.getLogger(SectorControlador.class);

    @Autowired
    private SectorServicio sectorServicio;

    /**
     * Lista todos los sectores disponibles.
     *
     * @return ResponseEntity que contiene una lista de sectores en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<SectorDto>> listarTodos() {
        LOGGER.debug("Listando todos los sectores");
        List<SectorDto> sectores = sectorServicio.listarTodos().stream()
            .map(this::convertirADto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sectores);
    }

    /**
     * Lista todos los sectores que están disponibles.
     *
     * @return ResponseEntity que contiene una lista de sectores disponibles en formato DTO.
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<SectorDto>> listarDisponibles() {
        LOGGER.debug("Listando sectores disponibles");
        List<SectorDto> sectores = sectorServicio.listarDisponibles().stream()
            .map(this::convertirADto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(sectores);
    }

    /**
     * Obtiene un sector por su ID.
     *
     * @param id ID del sector que se desea obtener.
     * @return ResponseEntity que contiene el sector en formato DTO, o un estado 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SectorDto> obtenerPorId(@PathVariable Long id) {
        LOGGER.debug("Buscando sector por ID: {}", id);
        return sectorServicio.buscarPorId(id)
            .map(sector -> ResponseEntity.ok(convertirADto(sector)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un sector por su nombre.
     *
     * @param nombre Nombre del sector que se desea obtener.
     * @return ResponseEntity que contiene el sector en formato DTO, o un estado 404 si no se encuentra.
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<SectorDto> obtenerPorNombre(@PathVariable String nombre) {
        LOGGER.debug("Buscando sector por nombre: {}", nombre);
        return sectorServicio.buscarPorNombre(nombre)
            .map(sector -> ResponseEntity.ok(convertirADto(sector)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza el estado de un sector específico.
     *
     * @param id ID del sector cuyo estado se desea actualizar.
     * @param estado Nuevo estado del sector.
     * @return ResponseEntity que contiene el sector actualizado en formato DTO, o un estado 404 si no se encuentra.
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<SectorDto> actualizarEstado(
            @PathVariable Long id,
            @RequestParam SectorEntidad.EstadoSector estado) {
        LOGGER.debug("Actualizando estado del sector {} a {}", id, estado);
        try {
            SectorEntidad sector = sectorServicio.actualizarEstado(id, estado);
            return ResponseEntity.ok(convertirADto(sector));
        } catch (RuntimeException e) {
            LOGGER.error("Error actualizando estado del sector: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Convierte una entidad de sector a un objeto DTO.
     *
     * @param sector Entidad de sector a convertir.
     * @return Objeto DTO que representa el sector.
     */
    private SectorDto convertirADto(SectorEntidad sector) {
        SectorDto dto = new SectorDto();
        dto.setId(sector.getId());
        dto.setNombre(sector.getNombre());
        dto.setMetrosCuadrados(sector.getMetrosCuadrados());
        dto.setPrecioMensual(sector.getPrecioMensual());
        dto.setCaracteristicas(sector.getCaracteristicas());
        dto.setEstado(sector.getEstado().name());
        return dto;
    }
}