package com.example.bibliocore.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bibliocore.modelo.Editorial;

public interface EditorialRepositorio extends JpaRepository<Editorial, Long>{
    // Verificar si existe una editorial por su nombre, ignorando mayúsculas y minúsculas
    boolean existsByNombreIgnoreCase(String nombre);

}
