package com.example.api_gestion_almacen.controladores;

import java.util.List;
import java.util.Map;

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
            System.out.println("UsuarioControlador.crearUsuario - Iniciando creación de usuario");
            System.out.println("Datos recibidos: " + crearUsuDTO.toString());
            
            CrearUsuDto nuevoUsuario = usuarioServicio.crearUsuario(crearUsuDTO);
            
            System.out.println("Usuario creado: " + (nuevoUsuario != null ? nuevoUsuario.toString() : "null"));
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage().contains("correo electrónico ya está registrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
            }
            throw e;
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
        System.out.println("UsuarioControlador.obtenerTodosLosUsuarios - Iniciando");
        List<UsuarioDto> usuarios = usuarioServicio.obtenerTodosLosUsuarios();
        System.out.println("UsuarioControlador.obtenerTodosLosUsuarios - Usuarios encontrados: " + (usuarios != null ? usuarios.size() : "null"));
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
            System.out.println("UsuarioControlador.actualizarUsuario - Iniciando actualización de usuario " + id);
            System.out.println("Datos recibidos: " + usuarioDTO.toString());
            
            CrearUsuDto usuarioActualizado = usuarioServicio.actualizarUsuario(id, usuarioDTO);
            
            System.out.println("Usuario actualizado: " + (usuarioActualizado != null ? usuarioActualizado.toString() : "null"));
            return ResponseEntity.ok(usuarioActualizado);
        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
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
    @DeleteMapping(path = "/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
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
            
            if (correoElectronico == null || correoElectronico.isEmpty() || contrasena == null || contrasena.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Correo electrónico y contraseña son requeridos"));
            }
            
            UsuarioDto usuarioAutenticado = usuarioServicio.autenticarUsuario(correoElectronico, contrasena);
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
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
            usuarioServicio.confirmarCorreoUsuario(email);
            return ResponseEntity.ok().body(Map.of("mensaje", "Correo confirmado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al confirmar correo: " + e.getMessage()));
        }
    }
    
}