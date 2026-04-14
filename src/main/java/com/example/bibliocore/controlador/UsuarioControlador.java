package com.example.bibliocore.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliocore.dto.LoginDTO;
import com.example.bibliocore.dto.UsuarioPasswordDTO;
import com.example.bibliocore.dto.UsuarioPerfilDTO;
import com.example.bibliocore.modelo.Usuario;
import com.example.bibliocore.servicio.UsuarioServicio;
import com.example.bibliocore.util.JwtUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador de operaciones sobre usuarios.
 */
@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/usuarios") // Ruta base para todos los endpoints de este controlador
public class UsuarioControlador {

    private final UsuarioServicio usuarioService;
    private final JwtUtil jwtUtil;

    /**
     * Inyecta los servicios necesarios del controlador.
     *
     * @param usuarioService servicio de usuarios.
     * @param jwtUtil        utilidad para generar tokens JWT.
     */
    public UsuarioControlador(UsuarioServicio usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra un usuario nuevo.
     *
     * @param usuario datos del usuario.
     * @return mensaje de resultado del registro.
     */
    @PostMapping("registrarUsuario")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        usuarioService.registrar(usuario);

        return ResponseEntity.ok("Usuario " + usuario.getUsername() + " registrado exitosamente");
    }

    /**
     * Autentica al usuario y devuelve un token JWT.
     *
     * @param loginDto credenciales de acceso.
     * @return token y datos básicos del usuario autenticado.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            Usuario usuario = usuarioService.login(loginDto.getUsername(), loginDto.getPassword());
            String token = jwtUtil.generarToken(usuario);

            // Añadir un token o algo
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "id", usuario.getId(),
                    "usuario", usuario.getUsername(),
                    "isAdmin", usuario.isAdmin()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * Modifica el perfil de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @param dto       datos del perfil a actualizar.
     * @return mensaje de éxito o 404 si no existe.
     */
    @PatchMapping("modificarUsuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> modificarUsuario(@PathVariable Long usuarioId, @RequestBody UsuarioPerfilDTO dto) {
        return usuarioService.modificarPerfil(usuarioId, dto.username, dto.email, dto.nombre).map(usuarioExistente -> {
            return ResponseEntity.ok("Usuario " + usuarioExistente.getUsername() + " modificado exitosamente");
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @param dto       datos de contraseña actual y nueva.
     * @return mensaje de resultado de la operación.
     */
    @PatchMapping("cambiarPassword/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> cambiarPassword(@PathVariable Long usuarioId, @RequestBody UsuarioPasswordDTO dto) {
        usuarioService.cambiarPassword(usuarioId, dto.password, dto.nuevaPassword, dto.confirmarPassword);
        return ResponseEntity.ok("Contraseña actualizada");
    }

    /**
     * Sanciona a un usuario por una cantidad de días.
     *
     * @param usuarioId identificador del usuario.
     * @param dias      duración de la sanción en días.
     * @return mensaje de éxito o 404 si no existe.
     */
    @PostMapping("/sancionarUsuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sancionarUsuario(@PathVariable Long usuarioId, @RequestParam int dias) {
        return usuarioService.sancionarUsuario(usuarioId, dias).map(usuarioExistente -> {
            return ResponseEntity.ok("Usuario " + usuarioExistente.getNombre() + " sancionado hasta "
                    + usuarioExistente.getFinSancion());
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Levanta la sanción de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return mensaje de éxito o 404 si no existe.
     */
    @PostMapping("/levantarSancion/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> levantarSancion(@PathVariable Long usuarioId) {
        return usuarioService.levantarSancion(usuarioId).map(usuarioExistente -> {
            return ResponseEntity.ok("Sanción levantada para el usuario " + usuarioExistente.getNombre());
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Activa un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return mensaje de resultado de la operación.
     */
    @PatchMapping("/activarUsuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activarUsuario(@PathVariable Long usuarioId) {
        usuarioService.activarUsuario(usuarioId);
        return ResponseEntity.ok("Usuario activado exitosamente");
    }

    /**
     * Desactiva un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return mensaje de resultado de la operación.
     */
    @PatchMapping("/desactivarUsuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> desactivarUsuario(@PathVariable Long usuarioId) {
        usuarioService.desactivarUsuario(usuarioId);
        return ResponseEntity.ok("Usuario desactivado exitosamente");
    }

    /**
     * Otorga privilegios de administrador a un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return mensaje de resultado de la operación.
     */
    @PatchMapping("/hacerAdmin/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> hacerAdmin(@PathVariable Long usuarioId) {
        usuarioService.hacerAdmin(usuarioId);
        return ResponseEntity.ok("Privilegios de administrador otorgados exitosamente");
    }

    /**
     * Revoca privilegios de administrador a un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return mensaje de resultado de la operación.
     */
    @PatchMapping("/quitarAdmin/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> quitarAdmin(@PathVariable Long usuarioId) {
        usuarioService.quitarAdmin(usuarioId);
        return ResponseEntity.ok("Privilegios de administrador revocados exitosamente");
    }

    /**
     * Busca usuarios por nombre.
     *
     * @param nombre nombre o fragmento para la búsqueda.
     * @return lista de usuarios coincidentes.
     */
    @GetMapping("/buscarPorNombre")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Usuario>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    /**
     * Busca usuarios por correo electrónico.
     *
     * @param email correo o fragmento para la búsqueda.
     * @return lista de usuarios coincidentes.
     */
    @GetMapping("/buscarPorEmail")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Usuario>> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
    }

    /**
     * Obtiene los usuarios con sanción activa.
     *
     * @return lista de usuarios sancionados.
     */
    @GetMapping("/buscarSancionados")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Usuario>> buscarSancionados() {
        return ResponseEntity.ok(usuarioService.buscarSancionados());
    }

    /**
     * Obtiene los usuarios sin sanción activa.
     *
     * @return lista de usuarios no sancionados.
     */
    @GetMapping("/buscarNoSancionados")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Usuario>> buscarNoSancionados() {
        return ResponseEntity.ok(usuarioService.buscarNoSancionados());
    }

}
