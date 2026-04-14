package com.example.bibliocore.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bibliocore.modelo.ListaDeseados;

public interface ListaDeseadosRepositorio extends JpaRepository<ListaDeseados, Long>{

    ListaDeseados findByUsuarioId(Long usuarioId);
}
