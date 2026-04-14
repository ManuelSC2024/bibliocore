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

import com.example.bibliocore.modelo.Genero;
import com.example.bibliocore.servicio.GeneroServicio;

@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/genero") // Ruta base para todos los endpoints de este controlador
public class GeneroControlador {
    private final GeneroServicio generoServicio;

    /**
     * Inyecta el servicio de géneros.
     *
     * @param generoServicio servicio usado por el controlador.
     */
    public GeneroControlador(GeneroServicio generoServicio) {
        this.generoServicio = generoServicio;
    }

    /**
     * Crea un género nuevo.
     *
     * @param genero datos del género a crear.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/crearGenero")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden crear géneros
    public ResponseEntity<String> crearGenero(@RequestBody Genero genero) {
        try {
            generoServicio.crearGenero(genero);
            return ResponseEntity.ok("Género " + genero.getNombre() + " añadido exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Modifica un género existente.
     *
     * @param editorialId identificador del género.
     * @param nombre      nuevo nombre del género.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/modificarGenero")
    @PreAuthorize("hasRole('ADMIN')") // Solo los administradores pueden modificar géneros
    public ResponseEntity<String> modificarGenero(@PathVariable Integer editorialId, @PathVariable String nombre) {
        try {
            generoServicio.modificarGenero(editorialId, nombre);
            return ResponseEntity.ok("Género modificado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
