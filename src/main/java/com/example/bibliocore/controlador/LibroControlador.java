package com.example.bibliocore.controlador;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliocore.dto.LibroDTO;
import com.example.bibliocore.dto.LibroPopularDTO;
import com.example.bibliocore.modelo.Libro;
import com.example.bibliocore.servicio.LibroServicio;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de operaciones sobre libros.
 */
@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/libros") // Ruta base para todos los endpoints de este controlador
public class LibroControlador {

    private final LibroServicio libroServicio;

    /**
     * Inyecta el servicio de libros.
     *
     * @param libroServicio servicio usado por el controlador.
     */
    public LibroControlador(LibroServicio libroServicio) {
        this.libroServicio = libroServicio;
    }

    /**
     * Crea un libro a partir de un DTO.
     *
     * @param dto datos del libro a crear.
     * @return mensaje de resultado de la operación.
     */
    @PostMapping("/crearLibro")
    public ResponseEntity<String> crearLibro(@RequestBody LibroDTO dto) {
        Libro nuevoLibro = libroServicio.crearLibro(dto);

        return ResponseEntity.ok("Libro '" + nuevoLibro.getTitulo() + "' engadido correctamente.");
    }

    /**
     * Obtiene todos los libros registrados.
     *
     * @return lista de libros.
     */
    @GetMapping("/obtenerTodos")
    public ResponseEntity<List<Libro>> obtenerTodos() {
        return ResponseEntity.ok(libroServicio.obtenerTodos());
    }

    /**
     * Busca un libro por su identificador.
     *
     * @param libroId identificador del libro.
     * @return libro encontrado o 404 si no existe.
     */
    @GetMapping("/buscarPorId/{libroId}") // Mejor usar PathVariable para IDs específicos
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long libroId) {
        return libroServicio.buscarPorId(libroId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Busca libros por título.
     *
     * @param titulo título o fragmento a buscar.
     * @return lista de libros coincidentes.
     */
    @GetMapping("/buscarPorTitulo")
    public ResponseEntity<List<Libro>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(libroServicio.buscarPorTitulo(titulo));
    }

    /**
     * Busca libros por nombre de autor.
     *
     * @param autor nombre o fragmento del autor.
     * @return lista de libros coincidentes.
     */
    @GetMapping("/buscarPorAutor")
    public ResponseEntity<List<Libro>> buscarPorAutor(@RequestParam String autor) {
        return ResponseEntity.ok(libroServicio.buscarPorAutor(autor));
    }

    @GetMapping("/obtenerPopulares/{cantidad}")
    public ResponseEntity<List<LibroPopularDTO>> obtenerPopulares(@PathVariable int cantidad) {
        return ResponseEntity.ok(libroServicio.obtenerPopulares(cantidad));
    }
}
