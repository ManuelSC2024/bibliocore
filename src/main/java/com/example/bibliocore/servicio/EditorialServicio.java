package com.example.bibliocore.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.modelo.Editorial;
import com.example.bibliocore.repositorio.EditorialRepositorio;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepo;

    /**
     * Crea una editorial si no existe otra con el mismo nombre.
     *
     * @param editorial datos de la editorial a guardar.
     * @return editorial creada.
     */
    @Transactional
    public Editorial crearEditorial(Editorial editorial) {
        if (editorialRepo.existsByNombreIgnoreCase(editorial.getNombre())) {
            throw new RuntimeException("La editorial " + editorial.getNombre() + " ya existe");
        }

        return editorialRepo.save(editorial);
    }

    /**
     * Modifica el nombre de una editorial existente.
     *
     * @param id     identificador de la editorial.
     * @param nombre nuevo nombre de la editorial.
     */
    @Transactional
    public void modificarEditorial(Long id, String nombre) {
        Editorial editorialExistente = editorialRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada"));

        editorialExistente.setNombre(nombre);
        editorialRepo.save(editorialExistente);
    }
}