package com.example.bibliocore.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.modelo.Autor;
import com.example.bibliocore.repositorio.AutorRepositorio;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepo;

    /**
     * Crea un autor si no existe otro con el mismo nombre.
     *
     * @param autor datos del autor a guardar.
     * @return autor creado.
     */
    @Transactional
    public Autor crearAutor(Autor autor) {
        if (autorRepo.existsByNombreIgnoreCase(autor.getNombre())) {
            throw new RuntimeException("El autor " + autor.getNombre() + " ya existe");
        }

        return autorRepo.save(autor);
    }

    /**
     * Modifica el nombre de un autor existente.
     *
     * @param id          identificador del autor.
     * @param nuevoNombre nuevo nombre del autor.
     */
    @Transactional
    public void modificarAutor(Long id, String nuevoNombre) {
        Autor autorExistente = autorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no Encontrado"));

        if (autorRepo.existsByNombreIgnoreCase(nuevoNombre)) {
            throw new RuntimeException("El autor " + nuevoNombre + " ya existe");
        }

        autorExistente.setNombre(nuevoNombre);
        autorRepo.save(autorExistente);
    }
}