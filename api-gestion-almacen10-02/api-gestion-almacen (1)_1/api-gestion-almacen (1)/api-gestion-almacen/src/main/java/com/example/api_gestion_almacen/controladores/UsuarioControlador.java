package com.example.api_gestion_almacen.controladores;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.example.api_gestion_almacen.dtos.CrearUsuDto;
import com.example.api_gestion_almacen.dtos.UsuarioDto;
import com.example.api_gestion_almacen.servicios.UsuarioServicio;

/**
 * Controlador REST para gestionar operaciones relacionadas con usuarios.
 * Proporciona endpoints para crear, obtener, actualizar y eliminar usuarios,
 * así como para autenticar usuarios.
 */
@RestController
@RequestMapping("api/usuarios")
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio; // Servicio para manejar la lógica de negocio relacionada con usuarios

    /**
     * Crea un nuevo usuario.
     * @author andres
     *
     * @param usuarioDTO El objeto que contiene la información del usuario a crear.
     * @return El usuario creado.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crearUsuario(@RequestBody CrearUsuDto crearUsuDTO) {
        try {
            logger.info("Iniciando creación de usuario: {}", crearUsuDTO.getCorreoElectronico());
            logger.debug("Datos recibidos: {}", crearUsuDTO);
            
            CrearUsuDto nuevoUsuario = usuarioServicio.crearUsuario(crearUsuDTO);
            
            logger.info("Usuario creado exitosamente: {}", nuevoUsuario.getCorreoElectronico());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            logger.error("Error al crear usuario: {}", e.getMessage(), e);
            if (e.getMessage().contains("correo electrónico ya está registrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear usuario: " + e.getMessage());
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * @author andres
     *
     * @param id El ID del usuario a obtener.
     * @return El usuario correspondiente al ID proporcionado.
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UsuarioDto obtenerUsuarioPorId(@PathVariable Long id) { 
        logger.info("Obteniendo usuario con ID: {}", id);
        return usuarioServicio.obtenerUsuarioPorId(id); 
    }

    /**
     * Obtiene todos los usuarios.
     * @author andres
     *
     * @return Una lista de todos los usuarios disponibles.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UsuarioDto> obtenerTodosLosUsuarios() { 
        logger.info("Obteniendo lista de todos los usuarios");
        List<UsuarioDto> usuarios = usuarioServicio.obtenerTodosLosUsuarios();
        logger.info("Total de usuarios encontrados: {}", usuarios.size());
        return usuarios;
    }

 

    /**
     * Actualiza un usuario existente.
     * @author andres
     *
     * @param id El ID del usuario a actualizar.
     * @param usuarioDTO El objeto que contiene la nueva información del usuario.
     * @return El usuario actualizado.
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody CrearUsuDto usuarioDTO) {
        try {
            logger.info("Iniciando actualización de usuario con ID: {}", id);
            logger.debug("Datos de actualización: {}", usuarioDTO);
            
            CrearUsuDto usuarioActualizado = usuarioServicio.actualizarUsuario(id, usuarioDTO);
            
            logger.info("Usuario actualizado exitosamente. ID: {}", id);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            logger.error("Error al actualizar usuario {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Elimina un usuario por su ID.
     * @author andres
     *
     * @param id El ID del usuario a eliminar.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            logger.info("Iniciando eliminación de usuario con ID: {}", id);
            usuarioServicio.eliminarUsuario(id);
            logger.info("Usuario eliminado exitosamente. ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error al eliminar usuario {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar usuario: " + e.getMessage());
        }
    }
    
    /**
     * Autentica a un usuario utilizando sus credenciales.
     * @author andres
     *
     * @param credenciales Un mapa que contiene el correo electrónico y la contraseña del usuario.
     * @return El usuario autenticado si las credenciales son válidas.
     */
    @PostMapping(path = "/autenticar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> autenticarUsuario(@RequestBody Map<String, String> credenciales) {
        try {
            String correoElectronico = credenciales.get("correoElectronico");
            String contrasena = credenciales.get("contrasena");
            
            logger.info("Intento de autenticación para usuario: {}", correoElectronico);
            
            if (correoElectronico == null || correoElectronico.isEmpty() || contrasena == null || contrasena.isEmpty()) {
                logger.warn("Intento de autenticación con credenciales incompletas");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Correo electrónico y contraseña son requeridos"));
            }
            
            UsuarioDto usuarioAutenticado = usuarioServicio.autenticarUsuario(correoElectronico, contrasena);
            logger.info("Autenticación exitosa para: {}", correoElectronico);
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (RuntimeException e) {
            logger.error("Error de autenticación: {}", e.getMessage());
            return ResponseEntity.status(401)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado durante la autenticación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
        }
    }

    /**
     * Confirma el correo electrónico de un usuario.
     * @author andres
     *
     * @param email El correo electrónico del usuario a confirmar.
     * @return ResponseEntity con el resultado de la operación.
     */
    @PostMapping("/confirmarCorreo/{email}")
    public ResponseEntity<?> confirmarCorreo(@PathVariable String email) {
        try {
            logger.info("Iniciando confirmación de correo para: {}", email);
            usuarioServicio.confirmarCorreoUsuario(email);
            logger.info("Correo confirmado exitosamente para: {}", email);
            return ResponseEntity.ok().body(Map.of("mensaje", "Correo confirmado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al confirmar correo para {}: {}", email, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al confirmar correo: " + e.getMessage()));
        }
    }

    /**
     * Actualiza la contraseña de un usuario.
     * @author andres
     *
     * @param datos Un mapa que contiene el correo electrónico y la nueva contraseña del usuario.
     * @return ResponseEntity con el resultado de la operación.
     */
    @PostMapping("/actualizarContrasena")
    public ResponseEntity<?> actualizarContrasena(@RequestBody Map<String, String> datos) {
        try {
            String correoElectronico = datos.get("correoElectronico");
            String nuevaContrasena = datos.get("nuevaContrasena");
            
            logger.info("Iniciando actualización de contraseña para usuario: {}", correoElectronico);
            
            if (correoElectronico == null || nuevaContrasena == null) {
                logger.warn("Intento de actualización de contraseña con datos incompletos");
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Correo electrónico y nueva contraseña son requeridos"));
            }
            
            usuarioServicio.actualizarContrasenaUsuario(correoElectronico, nuevaContrasena);
            logger.info("Contraseña actualizada exitosamente para: {}", correoElectronico);
            return ResponseEntity.ok()
                .body(Map.of("mensaje", "Contraseña actualizada exitosamente"));
                
        } catch (Exception e) {
            logger.error("Error al actualizar contraseña: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar contraseña: " + e.getMessage()));
        }
    }
}