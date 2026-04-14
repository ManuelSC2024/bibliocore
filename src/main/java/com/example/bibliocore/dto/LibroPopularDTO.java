package com.example.bibliocore.dto;

import com.example.bibliocore.modelo.Libro;

import lombok.Data;

@Data
public class LibroPopularDTO {
    private Libro libro;
    private long totalPrestamos;

    public LibroPopularDTO(Libro libro, long totalPrestamos) {
        this.libro = libro;
        this.totalPrestamos = totalPrestamos;
    }
}