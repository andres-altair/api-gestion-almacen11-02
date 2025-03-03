package com.example.api_gestion_almacen.servicios;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api_gestion_almacen.dtos.RolDto;
import com.example.api_gestion_almacen.entidades.RolEntidad;
import com.example.api_gestion_almacen.repositorio.RolRepositorio;

/**
 * Servicio para gestionar las operaciones relacionadas con los roles.
 * Proporciona métodos para crear, obtener, actualizar y eliminar roles.
 * @author andres
 */
@Service
public class RolServicio {

    @Autowired
    private RolRepositorio rolRepositorio; // Repositorio para acceder a los datos de roles

    /**
     * Crea un nuevo rol a partir de un RolDto.
     * @author andres
     * 
     * @param rolDTO El objeto DTO que contiene la información del rol a crear.
     * @return El objeto RolDto del rol creado.
     */
    public RolDto crearRol(RolDto rolDTO) {
        RolEntidad rolEntidad = aEntidad(rolDTO); // Convertir DTO a entidad
        RolEntidad nuevoRol = rolRepositorio.save(rolEntidad); // Guardar la entidad en la base de datos
        return aDto(nuevoRol); // Convertir entidad a DTO y devolver
    }

    /**
     * Obtiene un rol por su ID.
     * @author andres
     * 
     * @param id El ID del rol a obtener.
     * @return El objeto RolDto correspondiente al rol encontrado, o null si no se encuentra.
     */
    public RolDto obtenerRolPorId(Long id) {
        RolEntidad rolEntidad = rolRepositorio.findById(id).orElse(null); // Buscar la entidad por ID
        return rolEntidad != null ? aDto(rolEntidad) : null; // Convertir entidad a DTO o devolver null
    }

    /**
     * Obtiene todos los roles disponibles.
     * @author andres
     * 
     * @return Una lista de objetos RolDto que representan todos los roles.
     */
    public List<RolDto> obtenerTodosLosRoles() {
        List<RolEntidad> roles = rolRepositorio.findAll(); // Obtener todas las entidades de rol
        return roles.stream()
                .map(this::aDto) // Convertir cada entidad a DTO
                .collect(Collectors.toList()); // Recoger los DTOs en una lista
    }

    /**
     * Actualiza un rol existente.
     * @author andres
     * 
     * @param id El ID del rol a actualizar.
     * @param rolDTO El objeto DTO que contiene la nueva información del rol.
     * @return El objeto RolDto del rol actualizado.
     */
    public RolDto actualizarRol(Long id, RolDto rolDTO) {
        RolEntidad rolEntidad = aEntidad(rolDTO); // Convertir DTO a entidad
        rolEntidad.setId(id); // Asegurarse de que el ID se mantenga
        RolEntidad rolActualizado = rolRepositorio.save(rolEntidad); // Guardar la entidad actualizada
        return aDto(rolActualizado); // Convertir entidad a DTO y devolver
    }

    /**
     * Elimina un rol por su ID.
     * @author andres
     * 
     * @param id El ID del rol a eliminar.
     */
    public void eliminarRol(Long id) {
        rolRepositorio.deleteById(id); // Eliminar la entidad por ID
    }

    // Métodos de conversión

    /**
     * Convierte un RolDto a una RolEntidad.
     * @author andres
     * 
     * @param rolDTO El objeto DTO a convertir.
     * @return La entidad correspondiente.
     */
    private RolEntidad aEntidad(RolDto rolDTO) {
        RolEntidad rolEntidad = new RolEntidad(); // Crear una nueva entidad
        rolEntidad.setId(rolDTO.getId()); // Establecer el ID
        rolEntidad.setNombre(rolDTO.getNombre()); // Establecer el nombre
        return rolEntidad; // Devolver la entidad
    }

    /**
     * Convierte una RolEntidad a un RolDto.
     * @author andres
     * 
     * @param rolEntidad La entidad a convertir.
     * @return El objeto DTO correspondiente.
     */
    private RolDto aDto(RolEntidad rolEntidad) {
        RolDto rolDTO = new RolDto(); // Crear un nuevo DTO
        rolDTO.setId(rolEntidad.getId()); // Establecer el ID
        rolDTO.setNombre(rolEntidad.getNombre()); // Establecer el nombre
        return rolDTO; // Devolver el DTO
    }
}