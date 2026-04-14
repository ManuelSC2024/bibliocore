package com.example.bibliocore.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliocore.modelo.ListaDeseados;
import com.example.bibliocore.servicio.ListaDeseadosServicio;

import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin(origins = "*") // Nesesario para que cualquier aplicación externa consulte tu API
@RestController // Nesesario para que los datos se debuelvan en formato JSON
@RequestMapping("/api/listaDeseados") // Ruta base para todos los endpoints de este controlador
public class ListaDeseadosControlador {

    private final ListaDeseadosServicio listaDeseadosServicio;

    /**
     * Inyecta el servicio de lista de deseados.
     *
     * @param listaDeseadosServicio servicio usado por el controlador.
     */
    public ListaDeseadosControlador(ListaDeseadosServicio listaDeseadosServicio) {
        this.listaDeseadosServicio = listaDeseadosServicio;
    }

    /**
     * Carga la lista de deseados asociada a un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return lista de deseados del usuario.
     */
    @PostMapping("/cargarlistaDeseados/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ListaDeseados cargarlistaDeseados(@PathVariable Long usuarioId) {
        return listaDeseadosServicio.cargarlistaDeseados(usuarioId);
    }

    /**
     * Agrega o elimina un libro de la lista de deseados de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @param libroId   identificador del libro.
     * @return mensaje con el resultado de la operación.
     */
    @PostMapping("/addLibroAListaDeseados/{usuarioId}/{libroId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> addLibroAListaDeseados(@PathVariable Long usuarioId, @PathVariable Long libroId) {
        return ResponseEntity.ok(listaDeseadosServicio.addLibroAListaDeseados(usuarioId, libroId));
    }
}
