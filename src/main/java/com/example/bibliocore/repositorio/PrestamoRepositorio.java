package com.example.bibliocore.repositorio;

import com.example.bibliocore.modelo.Prestamo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    // Buscar prestamos de un usuario específico.
    List<Prestamo> findByUsuarioUsername(String username);

    // Verificar si un libro específico está actualmente prestado (es decir, no ha sido devuelto).
    Boolean existsByLibroIdAndFechaDevolucionIsNull(Long libroId);

    // Verificar si un usuario ya tiene activo ese mismo libro.
    Boolean existsByUsuarioIdAndLibroIdAndFechaDevolucionIsNull(Long usuarioId, Long libroId);

    // Contar cuántos préstamos activos (no devueltos) hay para un libro específico.
    Integer countByLibroIdAndFechaDevolucionIsNull(Long libroId);

    Integer countByUsuarioIdAndFechaDevolucionIsNull(Long usuarioId);
}