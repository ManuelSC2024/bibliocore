package com.example.bibliocore.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.modelo.Libro;
import com.example.bibliocore.modelo.ListaDeseados;
import com.example.bibliocore.repositorio.LibroRepositorio;
import com.example.bibliocore.repositorio.ListaDeseadosRepositorio;

@Service
public class ListaDeseadosServicio {
    @Autowired
    private ListaDeseadosRepositorio listaDeseadosRepo;

    @Autowired
    private LibroRepositorio libroRepo;

    /**
     * Carga la lista de deseados de un usuario.
     *
     * @param usuarioId identificador del usuario.
     * @return lista de deseados del usuario.
     */
    public ListaDeseados cargarlistaDeseados(Long usuarioId) {
        return listaDeseadosRepo.findByUsuarioId(usuarioId);
    }

    /**
     * Agrega o elimina un libro de la lista de deseados.
     *
     * @param usuarioId identificador del usuario.
     * @param libroId   identificador del libro.
     * @return mensaje con el resultado de la operación.
     */
    @Transactional
    public String addLibroAListaDeseados(Long usuarioId, Long libroId) {
        ListaDeseados listaDeseados = listaDeseadosRepo.findByUsuarioId(usuarioId);

        Boolean libroAgregado;

        if (listaDeseados == null) {
            throw new RuntimeException("Lista de deseados no encontrada para el usuario con ID: " + usuarioId);
        }

        Libro libro = libroRepo.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + libroId));

        libroAgregado = listaDeseados.agregarOQuitarLibro(libro);
        listaDeseadosRepo.save(listaDeseados);

        return libroAgregado ? "Libro agregado a la lista de deseados" : "Libro eliminado de la lista de deseados";
    }
}
