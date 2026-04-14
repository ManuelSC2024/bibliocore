package com.example.bibliocore.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.dto.LibroDTO;
import com.example.bibliocore.dto.LibroPopularDTO;
import com.example.bibliocore.modelo.Autor;
import com.example.bibliocore.modelo.Editorial;
import com.example.bibliocore.modelo.Genero;
import com.example.bibliocore.modelo.Libro;
import com.example.bibliocore.repositorio.AutorRepositorio;
import com.example.bibliocore.repositorio.EditorialRepositorio;
import com.example.bibliocore.repositorio.GeneroRepositorio;
import com.example.bibliocore.repositorio.LibroRepositorio;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepo;

    @Autowired
    private AutorRepositorio autorRepo;

    @Autowired
    private GeneroRepositorio generoRepo;

    @Autowired
    private EditorialRepositorio editorialRepo;

    /**
     * Crea un nuevo libro con los datos proporcionados en el DTO.
     *
     * @param dto datos del libro a crear.
     * @return libro creado.
     */
    @Transactional
    public Libro crearLibro(LibroDTO dto) {
        Libro nuevoLibro = new Libro();
        nuevoLibro.setIsbn(dto.isbn);
        nuevoLibro.setTitulo(dto.titulo);
        nuevoLibro.setSinopsis(dto.sinopsis);
        nuevoLibro.setAnnoPublicacion(dto.annoPublicacion);
        nuevoLibro.setAnnoEdicion(dto.annoEdicion);
        nuevoLibro.setEjemplares(dto.ejemplares);
        nuevoLibro.setEdicion(dto.edicion);
        nuevoLibro.setIdioma(dto.idioma);

        Editorial editorial = editorialRepo.findById(dto.editorialId)
                .orElseThrow(() -> new RuntimeException("Editorial no Encontrada con ID: " + dto.editorialId));
        nuevoLibro.setEditorial(editorial);

        List<Autor> listaAutores = autorRepo.findAllById(dto.autoresIds);
        nuevoLibro.setAutores(listaAutores);

        List<Genero> listaGeneros = generoRepo.findAllById(dto.generosIds);
        nuevoLibro.setGeneros(listaGeneros);

        return libroRepo.save(nuevoLibro);
    }

    /**
     * Obtiene todos los libros registrados.
     *
     * @return lista de libros.
     */
    public List<Libro> obtenerTodos() {
        return libroRepo.findAll();
    }

    /**
     * Busca un libro por identificador.
     *
     * @param libroId identificador del libro.
     * @return libro encontrado dentro de Optional.
     */
    public Optional<Libro> buscarPorId(Long libroId) {
        return libroRepo.findById(libroId);
    }

    /**
     * Busca libros por título.
     *
     * @param titulo título o fragmento para la búsqueda.
     * @return lista de libros coincidentes.
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepo.findByTituloContainingIgnoreCase(titulo);
    }

    /**
     * Busca libros por nombre de autor.
     *
     * @param autor nombre o fragmento del autor.
     * @return lista de libros coincidentes.
     */
    public List<Libro> buscarPorAutor(String autor) {
        return libroRepo.findByAutoresNombreContainingIgnoreCase(autor);
    }

    public List<LibroPopularDTO> obtenerPopulares(int cantidad) {
        LocalDate haceTresMeses = LocalDate.now().minusMonths(3);
        Pageable top = PageRequest.of(0, cantidad);

        return libroRepo.obtenerPopulares(haceTresMeses, top);
    }
}