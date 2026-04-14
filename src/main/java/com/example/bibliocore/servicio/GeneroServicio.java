package com.example.bibliocore.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bibliocore.modelo.Genero;
import com.example.bibliocore.repositorio.GeneroRepositorio;

@Service
public class GeneroServicio {

    @Autowired
    private GeneroRepositorio generoRepo;

    /**
     * Crea un género si no existe otro con el mismo nombre.
     *
     * @param genero datos del género a guardar.
     * @return género creado.
     */
    @Transactional
    public Genero crearGenero(Genero genero) {
        if (generoRepo.existsByNombreIgnoreCase(genero.getNombre())) {
            throw new RuntimeException("El género " + genero.getNombre() + " ya existe");
        }

        return generoRepo.save(genero);
    }

    /**
     * Modifica el nombre de un género existente.
     *
     * @param id     identificador del género.
     * @param nombre nuevo nombre del género.
     */
    @Transactional
    public void modificarGenero(Integer id, String nombre) {
        Genero generoExistente = generoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Género no encontrado"));

        generoExistente.setNombre(nombre);
        generoRepo.save(generoExistente);
    }
}