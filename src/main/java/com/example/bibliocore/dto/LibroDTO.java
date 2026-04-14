package com.example.bibliocore.dto;

import java.util.List;

public class LibroDTO {
    public String isbn;
    public String titulo;
    public String sinopsis;
    public int annoPublicacion;
    public int annoEdicion;
    public int ejemplares;
    public String edicion;
    public String idioma;
    public List<Long> autoresIds;
    public List<Integer> generosIds;
    public Long editorialId;
}
