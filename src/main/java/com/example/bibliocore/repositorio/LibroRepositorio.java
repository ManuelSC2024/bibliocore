package com.example.bibliocore.repositorio;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bibliocore.dto.LibroPopularDTO;
import com.example.bibliocore.modelo.Libro;

import jakarta.persistence.LockModeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {

    // Buscar por título ignorando mayúsculas y minúsculas
    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    // Buscar por nombre del autor ignorando mayúsculas y minúsculas
    List<Libro> findByAutoresNombreContainingIgnoreCase(String nombreAutor);

    // Buscar por nombre del género ignorando mayúsculas y minúsculas
    List<Libro> findByGenerosNombreIgnoreCase(String nombreGenero);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Bloquea la fila para escritura
    @Query("SELECT libro FROM Libro libro WHERE libro.id = :id")
    Optional<Libro> findByIdWithLock(Long id);

    @Query("SELECT new com.example.bibliocore.dto.LibroPopularDTO(p.libro, COUNT(p)) " +
           "FROM Prestamo p " +
           "WHERE p.fechaInicioPrestamo >= :fechaLimite " +
           "GROUP BY p.libro " +
           "ORDER BY COUNT(p) DESC")
    List<LibroPopularDTO> obtenerPopulares(@Param("fechaLimite") LocalDate fechaLimite, Pageable pageable);
}
