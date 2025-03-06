package com.example.api_gestion_almacen.servicios;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api_gestion_almacen.dtos.CrearUsuDto;
import com.example.api_gestion_almacen.dtos.UsuarioDto;
import com.example.api_gestion_almacen.entidades.RolEntidad;
import com.example.api_gestion_almacen.entidades.UsuarioEntidad;
import com.example.api_gestion_almacen.repositorio.RolRepositorio;
import com.example.api_gestion_almacen.repositorio.UsuarioRepositorio;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios.
 * Proporciona métodos para crear, obtener, actualizar, eliminar y autenticar usuarios.
 * @author andres
 */
@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio; // Repositorio para acceder a los datos de usuarios
    
    @Autowired
    private RolRepositorio rolRepositorio;

    /**
     * Crea un nuevo usuario a partir de un UsuarioDto.
     * @author andres
     * 
     * @param usuarioDTO El objeto DTO que contiene la información del usuario a crear.
     * @return El objeto UsuarioDto del usuario creado.
     */
    public CrearUsuDto crearUsuario(CrearUsuDto crearUsuDTO) {
        System.out.println("UsuarioServicio.crearUsuario - Iniciando");
        System.out.println("Datos recibidos: " + crearUsuDTO);
        
        // Verificar si el correo ya existe
        if (usuarioRepositorio.findByCorreoElectronico(crearUsuDTO.getCorreoElectronico()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }
        
        try {
            // 1. Convertir DTO a entidad
            UsuarioEntidad usuarioEntidad = aEntidad2(crearUsuDTO);
            System.out.println("Entidad creada: " + usuarioEntidad);
            
            // 2. Guardar en base de datos
            UsuarioEntidad usuarioGuardado = usuarioRepositorio.save(usuarioEntidad);
            System.out.println("Usuario guardado en BD: " + usuarioGuardado);
            
            // 3. Convertir entidad guardada a DTO
            CrearUsuDto usuarioCreado = new CrearUsuDto();
            usuarioCreado= aDto2(usuarioGuardado);
            
            System.out.println("DTO de respuesta: " + usuarioCreado);
            return usuarioCreado;
            
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear usuario: " + e.getMessage());
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * @author andres
     * 
     * @param id El ID del usuario a obtener.
     * @return El objeto UsuarioDto correspondiente al usuario encontrado, o null si no se encuentra.
     */
    public UsuarioDto obtenerUsuarioPorId(Long id) {
        UsuarioEntidad usuarioEntidad = usuarioRepositorio.findById(id).orElse(null); // Buscar la entidad por ID
        return usuarioEntidad != null ? aDto(usuarioEntidad) : null; // Convertir entidad a DTO o devolver null
    }

    /**
     * Obtiene todos los usuarios disponibles.
     * @author andres
     * 
     * @return Una lista de objetos UsuarioDto que representan todos los usuarios.
     */
    public List<UsuarioDto> obtenerTodosLosUsuarios() {
        System.out.println("API - UsuarioServicio.obtenerTodosLosUsuarios - Iniciando");
        try {
            List<UsuarioEntidad> usuarios = usuarioRepositorio.findAll();
            System.out.println("API - UsuarioServicio.obtenerTodosLosUsuarios - Usuarios encontrados en BD: " + usuarios.size());
            
            List<UsuarioDto> usuariosDto = usuarios.stream()
                .map(this::aDto)
                .collect(Collectors.toList());
            
            System.out.println("API - UsuarioServicio.obtenerTodosLosUsuarios - UsuariosDTO convertidos: " + usuariosDto.size());
            for (UsuarioDto dto : usuariosDto) {
                System.out.println("API - Usuario: " + dto.getId() + " - " + dto.getNombreCompleto() + " - Rol: " + dto.getRolId());
            }
            
            return usuariosDto;
        } catch (Exception e) {
            System.err.println("API - UsuarioServicio.obtenerTodosLosUsuarios - Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /* 
     * Actualiza un usuario existente.
     * @author andres
     * 
     * @param id El ID del usuario a actualizar.
     * @param usuarioDTO El objeto DTO que contiene la nueva información del usuario.
     * @return El objeto UsuarioDto del usuario actualizado.
     */
    public CrearUsuDto actualizarUsuario(Long id, CrearUsuDto usuarioDTO) {
        System.out.println("UsuarioServicio.actualizarUsuario - Iniciando actualización del usuario " + id);
        System.out.println("Datos recibidos: " + usuarioDTO);

        // 1. Verificar que el usuario existe
        UsuarioEntidad usuarioExistente = usuarioRepositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Verificar si el nuevo correo ya existe (si se está cambiando)
        if (!usuarioExistente.getCorreoElectronico().equals(usuarioDTO.getCorreoElectronico()) &&
            usuarioRepositorio.findByCorreoElectronico(usuarioDTO.getCorreoElectronico()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        try {
            // 3. Actualizar los campos del usuario
            usuarioExistente.setNombreCompleto(usuarioDTO.getNombreCompleto());
            usuarioExistente.setMovil(usuarioDTO.getMovil());
            usuarioExistente.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
            usuarioExistente.setGoogle(usuarioDTO.isGoogle());

            // 4. Actualizar el rol si ha cambiado
            if (usuarioDTO.getRolId() != null) {
                RolEntidad nuevoRol = rolRepositorio.findById(usuarioDTO.getRolId())
                    .orElseThrow(() -> new RuntimeException("El rol especificado no existe"));
                usuarioExistente.setRol(nuevoRol);
            }

            // 5. Actualizar la contraseña solo si se proporciona una nueva
            if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().trim().isEmpty()) {
                usuarioExistente.setContrasena(usuarioDTO.getContrasena());
            }

            // 6. Actualizar la foto si se proporciona una nueva
            if (usuarioDTO.getFoto() != null) {
                usuarioExistente.setFoto(usuarioDTO.getFoto());
            }

            // 7. Guardar los cambios
            UsuarioEntidad usuarioActualizado = usuarioRepositorio.save(usuarioExistente);
            System.out.println("Usuario actualizado en BD: " + usuarioActualizado);

            // 8. Convertir a DTO y devolver
            CrearUsuDto usuarioResponse = aDto2(usuarioActualizado);
            System.out.println("DTO de respuesta: " + usuarioResponse);
            return usuarioResponse;

        } catch (Exception e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    /** Elimina un usuario por su ID.
     * @author andres
     * 
     * @param id El ID del usuario a eliminar.
     */
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id); // Eliminar la entidad por ID
    }

    /**
     * Autentica un usuario verificando sus credenciales.
     * @author andres
     * 
     * @param correoElectronico El correo electrónico del usuario.
     * @param contrasena La contraseña del usuario.
     * @return El objeto UsuarioDto del usuario autenticado.
     * @throws RuntimeException Si las credenciales son inválidas.
     */
    public UsuarioDto autenticarUsuario(String correoElectronico, String contrasena) {
        Optional<UsuarioEntidad> usuarioOpt = usuarioRepositorio.findByCorreoElectronico(correoElectronico);
        
        if (!usuarioOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        UsuarioEntidad usuario = usuarioOpt.get();

         // Verificar correo confirmado
        if (!usuario.isCorreoConfirmado()) {
            throw new RuntimeException("Correo electrónico no confirmado");
        }

        // Verificar contrasena
        if (!contrasena.equals(usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        return aDto(usuario);
    }

    /**
     * Confirma el correo electrónico de un usuario.
     * @author andres
     * 
     * @param email El correo electrónico del usuario a confirmar
     * @throws RuntimeException si el usuario no existe
     */
    public void confirmarCorreoUsuario(String email) {
        UsuarioEntidad usuario = usuarioRepositorio.findByCorreoElectronico(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        usuario.setCorreoConfirmado(true);
        usuarioRepositorio.save(usuario);
        
        System.out.println("Correo confirmado para usuario: " + email);
    }

    /**
     * Actualiza la contraseña de un usuario.
     * @author andres
     * 
     * @param email El correo electrónico del usuario
     * @param nuevaContrasena La nueva contraseña a establecer
     * @throws RuntimeException Si el usuario no existe o hay un error al actualizar
     */
    public void actualizarContrasenaUsuario(String email, String nuevaContrasena) {
        System.out.println("UsuarioServicio.actualizarContrasenaUsuario - Iniciando actualización de contraseña para " + email);
        
        // 1. Buscar usuario por email
        UsuarioEntidad usuario = usuarioRepositorio.findByCorreoElectronico(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        try {
            // 2. Actualizar contraseña
            usuario.setContrasena(nuevaContrasena);
            
            // 3. Guardar cambios
            usuarioRepositorio.save(usuario);
            System.out.println("Contraseña actualizada exitosamente para: " + email);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar contraseña: " + e.getMessage());
        }
    }

    /**
     * Busca un usuario por su correo electrónico.
     * @author andres
     * 
     * @param correoElectronico El correo electrónico del usuario a buscar.
     * @return El objeto UsuarioDto del usuario encontrado, o null si no se encuentra.
     */
    public UsuarioDto buscarPorCorreo(String correoElectronico) {
        Optional<UsuarioEntidad> usuarioOpt = usuarioRepositorio.findByCorreoElectronico(correoElectronico);
        return usuarioOpt.map(this::aDto).orElse(null);
    }

    /**
     * Convierte un UsuarioDto a una UsuarioEntidad.
     * @author andres
     * 
     * @param usuarioDTO El objeto DTO a convertir.
     * @return La entidad correspondiente.
     */
    private UsuarioEntidad aEntidad(UsuarioDto usuarioDTO) {
        UsuarioEntidad usuarioEntidad = new UsuarioEntidad();
        usuarioEntidad.setId(usuarioDTO.getId());
        usuarioEntidad.setNombreCompleto(usuarioDTO.getNombreCompleto());
        usuarioEntidad.setMovil(usuarioDTO.getMovil());
        usuarioEntidad.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        
        // Crear un objeto RolEntidad solo si es necesario
        if (usuarioDTO.getRolId() != null) {
            RolEntidad rol = new RolEntidad();
            rol.setId(usuarioDTO.getRolId());
            usuarioEntidad.setRol(rol);
        }
        
        usuarioEntidad.setFoto(usuarioDTO.getFoto());
        usuarioEntidad.setFechaCreacion(usuarioDTO.getFechaCreacion());
        usuarioEntidad.setCorreoConfirmado(usuarioDTO.isCorreoConfirmado());
        usuarioEntidad.setGoogle(usuarioDTO.isGoogle());
        return usuarioEntidad;
    }
    private UsuarioEntidad aEntidad2(CrearUsuDto crearUsuDTO) {
        UsuarioEntidad usuarioEntidad = new UsuarioEntidad();
        usuarioEntidad.setNombreCompleto(crearUsuDTO.getNombreCompleto());
        usuarioEntidad.setMovil(crearUsuDTO.getMovil());
        usuarioEntidad.setCorreoElectronico(crearUsuDTO.getCorreoElectronico());
        
        // Cargar el rol completo desde la base de datos
        if (crearUsuDTO.getRolId() != null) {
            RolEntidad rol = rolRepositorio.findById(crearUsuDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("El rol especificado no existe"));
            usuarioEntidad.setRol(rol);
        } else {
            throw new RuntimeException("El rol es obligatorio");
        }
        
        usuarioEntidad.setContrasena(crearUsuDTO.getContrasena());
        usuarioEntidad.setFoto(crearUsuDTO.getFoto());
        usuarioEntidad.setGoogle(crearUsuDTO.isGoogle());
        usuarioEntidad.setCorreoConfirmado(crearUsuDTO.isCorreoConfirmado());
        
        return usuarioEntidad;
    }

    /**
     * Convierte una UsuarioEntidad a un UsuarioDto.
     * @author andres
     * 
     * @param usuarioEntidad La entidad a convertir.
     * @return El objeto DTO correspondiente.
     */
    private UsuarioDto aDto(UsuarioEntidad usuarioEntidad) {
        UsuarioDto usuarioDTO = new UsuarioDto(); // Crear un nuevo DTO
        usuarioDTO.setId(usuarioEntidad.getId()); // Establecer el ID
        usuarioDTO.setNombreCompleto(usuarioEntidad.getNombreCompleto()); // Establecer el nombre completo
        usuarioDTO.setMovil(usuarioEntidad.getMovil()); // Establecer el número de móvil
        usuarioDTO.setCorreoElectronico(usuarioEntidad.getCorreoElectronico()); // Establecer el correo electrónico
        usuarioDTO.setRolId(usuarioEntidad.getRol().getId());
        usuarioDTO.setFoto(usuarioEntidad.getFoto()); // Establecer la foto
        usuarioDTO.setFechaCreacion(usuarioEntidad.getFechaCreacion()); // Establecer la fecha de creación
        usuarioDTO.setCorreoConfirmado(usuarioEntidad.isCorreoConfirmado()); // Establecer el estado de confirmación del correo
        usuarioDTO.setGoogle(usuarioEntidad.isGoogle());
        return usuarioDTO; // Devolver el DTO
    }
    private CrearUsuDto aDto2(UsuarioEntidad usuarioEntidad) {
        CrearUsuDto usuarioDTO = new CrearUsuDto(); // Crear un nuevo DTO
        usuarioDTO.setNombreCompleto(usuarioEntidad.getNombreCompleto()); // Establecer el nombre completo
        usuarioDTO.setMovil(usuarioEntidad.getMovil()); // Establecer el número de móvil
        usuarioDTO.setCorreoElectronico(usuarioEntidad.getCorreoElectronico()); // Establecer el correo electrónico
        usuarioDTO.setRolId(usuarioEntidad.getRol().getId());
        usuarioDTO.setFoto(usuarioEntidad.getFoto()); // Establecer la foto
        usuarioDTO.setCorreoConfirmado(usuarioEntidad.isCorreoConfirmado()); // Establecer el estado de confirmación del correo
        usuarioDTO.setGoogle(usuarioEntidad.isGoogle());
        
        return usuarioDTO; // Devolver el DTO
    }
}