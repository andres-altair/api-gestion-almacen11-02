package com.example.api_gestion_almacen.dtos;

import java.math.BigDecimal;
import com.example.api_gestion_almacen.entidades.SectorEntidad;

public class SectorDto {
    private Long id;
    private String nombre;
    private Integer metrosCuadrados;
    private BigDecimal precioMensual;
    private String caracteristicas;
    private String estado;
    private boolean disponible;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getMetrosCuadrados() { return metrosCuadrados; }
    public void setMetrosCuadrados(Integer metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }
    
    public BigDecimal getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(BigDecimal precioMensual) { this.precioMensual = precioMensual; }
    
    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { 
        this.estado = estado;
        this.disponible = SectorEntidad.EstadoSector.DISPONIBLE.name().equals(estado);
    }
    
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}