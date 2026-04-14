package com.example.bibliocore.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bibliocore.modelo.Autor;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    // Verificar si existe un autor por su nombre, ignorando mayúsculas y minúsculas
    boolean existsByNombreIgnoreCase(String nombre);
}
