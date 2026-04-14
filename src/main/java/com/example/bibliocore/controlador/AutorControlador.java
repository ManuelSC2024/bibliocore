package com.example.bibliocore.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliocore.modelo.Autor;
import com.example.bibliocore.servicio.AutorServicio;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/autores") // Ruta base para todos los endpoints de este controlador
public class AutorControlador {

    private final AutorServicio autorServicio;

    /**
     * Inyecta el servicio de autores.
     *
     * @param autorServicio servicio usado por el controlador.
     */
    public AutorControlador(AutorServicio autorServicio) {
        this.autorServicio = autorServicio;
    }

    /**
     * Crea un autor nuevo.
     *
     * @param autor datos del autor a crear.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/crearAutor")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear autores
    public ResponseEntity<String> crearAutor(@RequestBody Autor autor) {
        try {
            autorServicio.crearAutor(autor);
            return ResponseEntity.ok("Autor " + autor.getNombre() + " añadido exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Modifica un autor existente.
     *
     * @param autor datos del autor con id y nombre actualizados.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/modificarAutor")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden modificar autores
    public ResponseEntity<String> modificarAutor(@RequestBody Autor autor) {
        try {
            autorServicio.modificarAutor(autor.getId(), autor.getNombre());
            return ResponseEntity.ok("Autor modificado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
