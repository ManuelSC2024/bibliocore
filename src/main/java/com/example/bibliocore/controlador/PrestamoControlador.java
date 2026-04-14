package com.example.bibliocore.controlador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliocore.dto.SolicitarPrestamoDTO;
import com.example.bibliocore.dto.SolicitarPrestamoResponseDTO;
import com.example.bibliocore.modelo.Prestamo;
import com.example.bibliocore.servicio.PrestamoServicio;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de operaciones sobre préstamos.
 */
@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/prestamos") // Ruta base para todos los endpoints de este controlador
public class PrestamoControlador {

    private final PrestamoServicio prestamoServicio;

    /**
     * Inyecta el servicio de préstamos.
     *
     * @param prestamoServicio servicio usado por el controlador.
     */
    public PrestamoControlador(PrestamoServicio prestamoServicio) {
        this.prestamoServicio = prestamoServicio;
    }

    /**
     * Comprueba si un libro tiene ejemplares disponibles para préstamo.
     *
     * @param libroId identificador del libro.
     * @return true si hay disponibilidad, false en caso contrario.
     */
    @GetMapping("/libroDisponible/{libroId}")
    public ResponseEntity<Boolean> libroDisponible(@PathVariable Long libroId) {
        try {
            return ResponseEntity.ok(prestamoServicio.libroDisponible(libroId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    /**
     * Solicita un préstamo para un usuario y un libro.
     *
     * @param request datos de la solicitud de préstamo.
     * @return respuesta con el resultado de la operación.
     */
    @PostMapping("/solicitarPrestamo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SolicitarPrestamoResponseDTO> solicitarPrestamo(@RequestBody SolicitarPrestamoDTO request) {
        try {
            prestamoServicio.crearPrestamo(request.getUsuarioId(), request.getLibroId());
            return ResponseEntity.ok(new SolicitarPrestamoResponseDTO("Préstamo solicitado con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new SolicitarPrestamoResponseDTO(e.getMessage()));
        }
    }

    /**
     * Registra la devolución de un préstamo.
     *
     * @param prestamoId identificador del préstamo.
     * @return respuesta con el resultado de la operación.
     */
    @PostMapping("/devolverLibro/{prestamoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> devolverLibro(@PathVariable Long prestamoId) {
        try {
            prestamoServicio.devolverLibro(prestamoId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al devolver: " + e.getMessage());
        }
        return ResponseEntity.ok("Libro devuelto con éxito. Se han procesado posibles sanciones.");
    }

    /**
     * Consulta el historial de préstamos de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return lista de préstamos del usuario.
     */
    @GetMapping("/historial/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Prestamo>> verHistorial(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(prestamoServicio.verHistorialUsuario(usuarioId));
    }

}
