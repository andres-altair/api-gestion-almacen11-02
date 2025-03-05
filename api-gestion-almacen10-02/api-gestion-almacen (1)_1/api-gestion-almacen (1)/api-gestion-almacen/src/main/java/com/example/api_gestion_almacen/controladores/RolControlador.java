package com.example.api_gestion_almacen.controladores;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RolControlador.class);

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
    public ResponseEntity<?> crearRol(@RequestBody RolDto rolDTO) { 
        try {
            logger.info("Iniciando creación de rol: {}", rolDTO.getNombre());
            logger.debug("Datos del rol a crear: {}", rolDTO);
            
            RolDto nuevoRol = rolServicio.crearRol(rolDTO);
            
            logger.info("Rol creado exitosamente: {}", nuevoRol.getNombre());
            return ResponseEntity.ok(nuevoRol);
        } catch (Exception e) {
            logger.error("Error al crear rol: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al crear rol: " + e.getMessage());
        }
    }

    /**
     * Obtiene un rol por su ID.
     * @author andres
     *
     * @param id El ID del rol a obtener.
     * @return El rol correspondiente al ID proporcionado.
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtenerRolPorId(@PathVariable Long id) { 
        try {
            logger.info("Buscando rol con ID: {}", id);
            RolDto rol = rolServicio.obtenerRolPorId(id);
            
            if (rol != null) {
                logger.info("Rol encontrado: {}", rol.getNombre());
                return ResponseEntity.ok(rol);
            } else {
                logger.warn("No se encontró rol con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al buscar rol con ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al buscar rol: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los roles.
     * @author andres
     *
     * @return Una lista de todos los roles disponibles.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> obtenerTodosLosRoles() { 
        try {
            logger.info("Obteniendo lista de todos los roles");
            List<RolDto> roles = rolServicio.obtenerTodosLosRoles();
            logger.info("Total de roles encontrados: {}", roles.size());
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            logger.error("Error al obtener roles: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al obtener roles: " + e.getMessage());
        }
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
    public ResponseEntity<?> actualizarRol(@PathVariable Long id, @RequestBody RolDto rolDTO) {
        try {
            logger.info("Iniciando actualización de rol con ID: {}", id);
            logger.debug("Nuevos datos del rol: {}", rolDTO);
            
            RolDto rolActualizado = rolServicio.actualizarRol(id, rolDTO);
            
            logger.info("Rol actualizado exitosamente. ID: {}, Nombre: {}", id, rolActualizado.getNombre());
            return ResponseEntity.ok(rolActualizado);
        } catch (Exception e) {
            logger.error("Error al actualizar rol {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al actualizar rol: " + e.getMessage());
        }
    }

    /**
     * Elimina un rol por su ID.
     * @author andres
     *
     * @param id El ID del rol a eliminar.
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id) {
        try {
            logger.info("Iniciando eliminación de rol con ID: {}", id);
            rolServicio.eliminarRol(id);
            logger.info("Rol eliminado exitosamente. ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar rol {}: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error al eliminar rol: " + e.getMessage());
        }
    }
}