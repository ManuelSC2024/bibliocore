package com.example.bibliocore.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bibliocore.modelo.Genero;

public interface GeneroRepositorio extends JpaRepository<Genero, Integer>{
    // Verificar si existe un género por su nombre, ignorando mayúsculas y minúsculas
    boolean existsByNombreIgnoreCase(String nombre);
}
    