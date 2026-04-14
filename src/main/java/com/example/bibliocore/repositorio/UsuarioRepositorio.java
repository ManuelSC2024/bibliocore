package com.example.bibliocore.repositorio;

import com.example.bibliocore.modelo.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    // Buscar por username ignorando mayúsculas y minúsculas
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por username exacto
    Optional<Usuario> findByUsername(String username);

    // Buscar por email ignorando mayúsculas y minúsculas
    List<Usuario> findByEmailContainingIgnoreCase(String email);

    // Buscar usuarios sancionados o no sancionados
    List<Usuario> findBySancionadoTrue();

    // Buscar usuarios no sancionados
    List<Usuario> findBySancionadoFalse();

    // Verificar si existe un usuario por su username
    boolean existsByUsername(String username);

    // Verificar si existe un usuario por su email
    boolean existsByEmail(String email);

}
