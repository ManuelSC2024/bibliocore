package com.example.bibliocore.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.modelo.Libro;
import com.example.bibliocore.modelo.Prestamo;
import com.example.bibliocore.modelo.Usuario;
import com.example.bibliocore.repositorio.LibroRepositorio;
import com.example.bibliocore.repositorio.PrestamoRepositorio;
import com.example.bibliocore.repositorio.UsuarioRepositorio;

@Service
public class PrestamoServicio {

    @Autowired // Crea Singleton automaticamente entre otras cosas.
    private PrestamoRepositorio prestamoRepo;

    @Autowired
    private UsuarioRepositorio usuarioRepo;

    @Autowired
    private LibroRepositorio librorepo;

    /**
     * Comprueba si un libro tiene ejemplares disponibles.
     *
     * @param libroId identificador del libro.
     * @return true si hay ejemplares disponibles.
     */
    @Transactional(readOnly = true)
    public Boolean libroDisponible(Long libroId) {
        // Bloqueo pesimista para evitar condiciones de carrera
        Libro libro = librorepo.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no Encontrado"));

        // Contar préstamos activos
        Integer prestamosActivos = prestamoRepo.countByLibroIdAndFechaDevolucionIsNull(libroId);

        return prestamosActivos < libro.getEjemplares();
    }

    /**
     * Valida disponibilidad para préstamo usando bloqueo pesimista.
     *
     * @param libroId identificador del libro.
     * @return libro listo para préstamo.
     */
    @Transactional(readOnly = true)
    public Libro libroDisponiblePrestamo(Long libroId) {
        // Bloqueo pesimista para evitar condiciones de carrera
        Libro libro = librorepo.findByIdWithLock(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no Encontrado"));

        // Contar préstamos activos
        Integer prestamosActivos = prestamoRepo.countByLibroIdAndFechaDevolucionIsNull(libroId);

        // Compara las cantidades.
        if (prestamosActivos >= libro.getEjemplares()) {
            throw new RuntimeException("No hay ejemplares disponibles del libro: " + libro.getTitulo());
        }

        return libro;
    }

    /**
     * Crea un préstamo validando estado del usuario y disponibilidad.
     *
     * @param usuarioId identificador del usuario.
     * @param libroId   identificador del libro.
     */
    @Transactional
    public void crearPrestamo(Long usuarioId, Long libroId) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no Encontrado"));

        // Validaciones de usuario
        if (!usuario.isActivo()) {
            throw new RuntimeException("El usuario no está activo.");
        }

        if (!usuario.puedePedirPrestado()) {
            throw new RuntimeException("El usuario está SANCIONADO.");
        }

        Integer prestamosActivos = prestamoRepo.countByUsuarioIdAndFechaDevolucionIsNull(usuarioId);
        if (prestamosActivos >= 3) {
            throw new RuntimeException("El usuario ya tiene el máximo de 3 préstamos activos.");
        }

        if (prestamoRepo.existsByUsuarioIdAndLibroIdAndFechaDevolucionIsNull(usuarioId, libroId)) {
            throw new RuntimeException("El usuario ya tiene un ejemplar sin devolver de este libro.");
        }

        // Al estar dentro de @Transactional, el bloqueo de verificarDisponibilidad
        // persiste
        Libro libro = libroDisponiblePrestamo(libroId);

        Prestamo p = new Prestamo(usuario, libro);
        prestamoRepo.save(p);
    }

    /**
     * Registra la devolución de un préstamo.
     *
     * @param idPrestamo identificador del préstamo.
     */
    @Transactional // Solo guarda si se ejecuta la consulta al completo
    public void devolverLibro(Long idPrestamo) {
        Prestamo p = prestamoRepo.findById(idPrestamo)
                .orElseThrow(() -> new RuntimeException("Prestamo no Encontrado"));

        if (p.getFechaDevolucion() != null) {
            throw new RuntimeException("Este préstamo ya fue devuelto anteriormente.");
        }

        p.devolucion();
        prestamoRepo.save(p);
        usuarioRepo.save(p.getUsuario());
    }

    /**
     * Consulta el historial de préstamos de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return lista de préstamos del usuario.
     */
    public List<Prestamo> verHistorialUsuario(Long usuarioId) {
        return prestamoRepo.findByUsuarioId(usuarioId);
    }
}
