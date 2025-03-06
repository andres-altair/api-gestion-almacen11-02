package com.example.api_gestion_almacen.controladores;

import com.example.api_gestion_almacen.dtos.AlquilerDto;
import com.example.api_gestion_almacen.entidades.AlquilerEntidad;
import com.example.api_gestion_almacen.servicios.AlquilerServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerControlador {

    @Autowired
    private AlquilerServicio alquilerServicio;

    @PostMapping
    public ResponseEntity<AlquilerDto> crearAlquiler(@RequestBody AlquilerDto dto) {
        AlquilerEntidad alquiler = alquilerServicio.crearAlquiler(
            dto.getSectorId(),
            dto.getUsuarioId(),
            dto.getOrdenId(),
            dto.getMontoPagado(),
            dto.getFechaInicio(),
            dto.getFechaFin()
        );
        return ResponseEntity.ok(convertirADto(alquiler));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AlquilerDto>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<AlquilerDto> alquileres = alquilerServicio.listarAlquileresUsuario(usuarioId)
            .stream()
            .map(this::convertirADto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(alquileres);
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarAlquiler(@PathVariable Long id) {
        alquilerServicio.finalizarAlquiler(id);
        return ResponseEntity.ok().build();
    }

    private AlquilerDto convertirADto(AlquilerEntidad alquiler) {
        AlquilerDto dto = new AlquilerDto();
        dto.setId(alquiler.getId());
        dto.setSectorId(alquiler.getSector().getId());
        dto.setSectorNombre(alquiler.getSector().getNombre());
        dto.setUsuarioId(alquiler.getUsuarioId());
        dto.setFechaInicio(alquiler.getFechaInicio());
        dto.setFechaFin(alquiler.getFechaFin());
        dto.setMontoPagado(alquiler.getMontoPagado());
        dto.setOrdenId(alquiler.getOrdenId());
        dto.setEstado(alquiler.getEstado().name());
        return dto;
    }
}