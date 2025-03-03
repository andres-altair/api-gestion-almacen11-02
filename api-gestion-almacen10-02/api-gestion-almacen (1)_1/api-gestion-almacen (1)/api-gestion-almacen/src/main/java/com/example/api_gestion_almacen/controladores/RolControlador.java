package com.example.api_gestion_almacen.controladores;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.api_gestion_almacen.dtos.RolDto; 
import com.example.api_gestion_almacen.servicios.RolServicio; 
/**
 * Controlador REST para gestionar operaciones relacionadas con roles.
 * Proporciona endpoints para crear, obtener, actualizar y eliminar roles
 * @author andres
 */
@RestController
@RequestMapping("api/roles")
public class RolControlador {

    @Autowired
    private RolServicio rolServicio; 

    /**
     * Crea un nuevo rol.
     * @author andres
     *
     * @param rolDTO El objeto que contiene la información del rol a crear.
     * @return El rol creado.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RolDto crearRol(@RequestBody RolDto rolDTO) { 
        return rolServicio.crearRol(rolDTO); 
    }

    /**
     * Obtiene un rol por su ID.
     * @author andres
     *
     * @param id El ID del rol a obtener.
     * @return El rol correspondiente al ID proporcionado.
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RolDto obtenerRolPorId(@PathVariable Long id) { 
        return rolServicio.obtenerRolPorId(id); 
    }

    /**
     * Obtiene todos los roles.
     * @author andres
     *
     * @return Una lista de todos los roles disponibles.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RolDto> obtenerTodosLosRoles() { 
        return rolServicio.obtenerTodosLosRoles(); 
    }

    /**
     * Actualiza un rol existente.
     * @author andres
     *
     * @param id El ID del rol a actualizar.
     * @param rolDTO El objeto que contiene la nueva información del rol.
     * @return El rol actualizado.
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RolDto actualizarRol(@PathVariable Long id, @RequestBody RolDto rolDTO) {
        return rolServicio.actualizarRol(id, rolDTO); 
    }

    /**
     * Elimina un rol por su ID.
     * @author andres
     *
     * @param id El ID del rol a eliminar.
     */
    @DeleteMapping(path = "/{id}")
    public void eliminarRol(@PathVariable Long id) {
        rolServicio.eliminarRol(id); 
    }
}