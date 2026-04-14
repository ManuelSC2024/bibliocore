package com.example.bibliocore.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.modelo.ListaDeseados;
import com.example.bibliocore.modelo.Usuario;
import com.example.bibliocore.repositorio.ListaDeseadosRepositorio;
import com.example.bibliocore.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private ListaDeseadosRepositorio listaDeseadosRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Autentica un usuario con username y contraseña.
     * Si está sancionado con fecha vencida, limpia la sanción automáticamente.
     *
     * @param username nombre de usuario.
     * @param password contraseña en texto plano.
     * @return usuario autenticado.
     */
    public Usuario login(String username, String password) {
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));

        if (!encoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña Incorrecta");
        }

        if (!usuario.isActivo()) {
            throw new RuntimeException("El usuario " + usuario.getNombre() + " no está activo.");
        }

        if (usuario.isSancionado()) {
            if (usuario.getFinSancion() != null && usuario.getFinSancion().isBefore(LocalDate.now())) {
                // La sanción ha expirado, levantar la sanción automáticamente
                usuario.setSancionado(false);
                usuario.setFinSancion(null);
                usuarioRepo.save(usuario);
            } else {
                throw new RuntimeException("El usuario " + usuario.getNombre() +
                        " está SANCIONADO hasta el " + usuario.getFinSancion());
            }
        }

        return usuario;
    }

    /**
     * Registra un usuario y crea su lista de deseados inicial.
     * Verifica unicidad de username y email antes de guardar.
     *
     * @param usuario datos del usuario a registrar.
     * @return usuario registrado.
     */
    @Transactional
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepo.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El username " + usuario.getUsername() + " ya está registrado");
        }

        if (usuarioRepo.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email " + usuario.getEmail() + " ya está registrado");
        }

        usuario.setPassword(encoder.encode(usuario.getPassword()));
        Usuario usuarioGuardado = usuarioRepo.save(usuario);

        ListaDeseados listaDeseados = new ListaDeseados();
        listaDeseados.setUsuario(usuarioGuardado);

        listaDeseadosRepo.save(listaDeseados);

        return usuarioGuardado;
    }

    /**
     * Modifica los campos de perfil no nulos de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @param username  nuevo username opcional.
     * @param email     nuevo email opcional.
     * @param nombre    nuevo nombre opcional.
     * @return usuario modificado dentro de Optional.
     */
    @Transactional
    public Optional<Usuario> modificarPerfil(Long usuarioId, String username, String email, String nombre) {
        return usuarioRepo.findById(usuarioId).map(usuarioExistente -> {
            if (username != null) {
                usuarioExistente.setUsername(username);
            }

            if (email != null) {
                usuarioExistente.setEmail(email);
            }

            if (nombre != null) {
                usuarioExistente.setNombre(nombre);
            }

            return usuarioRepo.save(usuarioExistente);
        });
    }

    /**
     * Cambia la contraseña validando contraseña actual y confirmación.
     *
     * @param id           identificador del usuario.
     * @param actual       contraseña actual.
     * @param nueva        nueva contraseña.
     * @param confirmacion confirmación de la nueva contraseña.
     */
    @Transactional
    public void cambiarPassword(Long id, String actual, String nueva, String confirmacion) {
        Usuario usuario = usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));

        if (!nueva.equals(confirmacion))
            throw new RuntimeException("Las contraseñas no coinciden");
        if (!encoder.matches(actual, usuario.getPassword()))
            throw new RuntimeException("La contraseña actual es incorrecta");

        usuario.setPassword(encoder.encode(nueva));
        usuarioRepo.save(usuario);
    }

    /**
     * Marca al usuario como sancionado por un número de días.
     *
     * @param usuarioId identificador del usuario.
     * @param dias      duración de la sanción.
     * @return usuario sancionado dentro de Optional.
     */
    @Transactional
    public Optional<Usuario> sancionarUsuario(Long usuarioId, int dias) {
        return usuarioRepo.findById(usuarioId).map(usuarioExistente -> {
            usuarioExistente.setSancionado(true);
            usuarioExistente.setFinSancion(LocalDate.now().plusDays(dias));
            return usuarioRepo.save(usuarioExistente);
        });
    }

    /**
     * Elimina la sanción activa de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return usuario actualizado dentro de Optional.
     */
    @Transactional
    public Optional<Usuario> levantarSancion(Long usuarioId) {
        return usuarioRepo.findById(usuarioId).map(usuarioExistente -> {
            usuarioExistente.setSancionado(false);
            usuarioExistente.setFinSancion(null);
            return usuarioRepo.save(usuarioExistente);
        });
    }

    /**
     * Activa un usuario.
     *
     * @param usuarioId identificador del usuario.
     */
    @Transactional
    public void activarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
        usuario.setActivo(true);
        usuarioRepo.save(usuario);
    }

    /**
     * Desactiva un usuario.
     *
     * @param usuarioId identificador del usuario.
     */
    @Transactional
    public void desactivarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));

        usuario.setActivo(false);
        usuarioRepo.save(usuario);
    }

    /**
     * Otorga privilegios de administrador a un usuario.
     *
     * @param usuarioId identificador del usuario.
     */
    @Transactional
    public void hacerAdmin(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
        usuario.setAdmin(true);
        usuarioRepo.save(usuario);
    }

    /**
     * Revoca privilegios de administrador a un usuario.
     *
     * @param usuarioId identificador del usuario.
     */
    @Transactional
    public void quitarAdmin(Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));
        usuario.setAdmin(false);
        usuarioRepo.save(usuario);
    }

    /**
     * Busca usuarios por nombre.
     *
     * @param nombre nombre o fragmento para buscar.
     * @return lista de usuarios coincidentes.
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepo.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca usuarios por correo electrónico.
     *
     * @param email correo o fragmento para buscar.
     * @return lista de usuarios coincidentes.
     */
    public List<Usuario> buscarPorEmail(String email) {
        return usuarioRepo.findByEmailContainingIgnoreCase(email);
    }

    /**
     * Obtiene usuarios con sanción activa.
     *
     * @return lista de usuarios sancionados.
     */
    public List<Usuario> buscarSancionados() {
        return usuarioRepo.findBySancionadoTrue();
    }

    /**
     * Obtiene usuarios sin sanción activa.
     *
     * @return lista de usuarios no sancionados.
     */
    public List<Usuario> buscarNoSancionados() {
        return usuarioRepo.findBySancionadoFalse();
    }
}
