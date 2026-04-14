package com.example.bibliocore.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliocore.modelo.Editorial;
import com.example.bibliocore.servicio.EditorialServicio;

@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/editorial") // Ruta base para todos los endpoints de este controlador
public class EditorialControlador {

    private final EditorialServicio editorialServicio;

    /**
     * Inyecta el servicio de editoriales.
     *
     * @param editorialServicio servicio usado por el controlador.
     */
    public EditorialControlador(EditorialServicio editorialServicio) {
        this.editorialServicio = editorialServicio;
    }

    /**
     * Crea una editorial nueva.
     *
     * @param editorial datos de la editorial a crear.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/crearEditorial")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear editoriales
    public ResponseEntity<String> crearEditorial(@RequestBody Editorial editorial) {
        try {
            editorialServicio.crearEditorial(editorial);
            return ResponseEntity.ok("Editorial creada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Modifica una editorial existente.
     *
     * @param editorialId identificador de la editorial.
     * @param nombre      nuevo nombre de la editorial.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/modificarEditorial")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden modificar editoriales
    public ResponseEntity<String> modificarEditorial(@PathVariable Long editorialId, @PathVariable String nombre) {
        try {
            editorialServicio.modificarEditorial(editorialId, nombre);
            return ResponseEntity.ok("Editorial modificada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
