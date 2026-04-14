package com.example.bibliocore.modelo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// Orden personalizado de los campos en JSON
@JsonPropertyOrder({ "id", "isbn", "titulo", "autores", "sinopsis", "editorial", "generos", "edicion", "idioma",
        "annoPublicacion", "annoEdicion", "rutaPortada" })
@Entity
@Table(name = "libros")
@Data // Esto es de Lombok crea los getters y setters automáticamente
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Solo se muestra en JSON, no se puede modificar desde la solicitud
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String titulo;
    private String sinopsis;

    @Column(unique = true, nullable = false)
    private String isbn;
    private String edicion;
    private String idioma;
    private Integer annoPublicacion;
    private Integer annoEdicion;

    // Ruta de la portada
    private String rutaPortada;

    // Los libros pertenecen a una editorial
    @ManyToOne
    @JoinColumn(name = "editorial_id")
    private Editorial editorial;

    // Los libros pueden tener muchos autores
    @ManyToMany
    @JoinTable(name = "libro_autor", joinColumns = @JoinColumn(name = "libro_id"), inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores;

    // Los libros pueden pertenecer a muchos géneros
    @ManyToMany
    @JoinTable(name = "libro_genero", joinColumns = @JoinColumn(name = "libro_id"), inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private List<Genero> generos;

    @Column(nullable = false)
    private Integer ejemplares = 1;

    // Constructor vacío necesario para JPA
    public Libro() {
    }

    /**
     * Constructor para crear un nuevo libro. Todos los campos son necesarios
     * excepto la ruta de la portada, que es opcional.
     * 
     * @param titulo          El título del libro.
     * @param sinopsis        Una breve descripción del libro.
     * @param isbn            El número ISBN del libro, que debe ser único.
     * @param edicion         La edición del libro (por ejemplo, "Primera edición").
     * @param idioma          El idioma en el que está escrito el libro.
     * @param annoPublicacion El año en que se publicó el libro por primera vez.
     * @param annoEdicion     El año en que se publicó la edición actual del libro.
     * @param rutaPortada     La ruta de la imagen de la portada del libro.
     * @param editorial       La editorial a la que pertenece el libro.
     * @param autores         La lista de autores que escribieron el libro.
     * @param generos         La lista de géneros a los que pertenece el libro.
     */
    public Libro(String titulo, String sinopsis, String isbn, String edicion, String idioma, Integer annoPublicacion,
            Integer annoEdicion, Integer ejemplares, String rutaPortada, Editorial editorial, List<Autor> autores, List<Genero> generos) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.isbn = isbn;
        this.edicion = edicion;
        this.idioma = idioma;
        this.annoPublicacion = annoPublicacion;
        this.annoEdicion = annoEdicion;
        this.ejemplares = ejemplares;
        this.rutaPortada = rutaPortada;
        this.editorial = editorial;
        this.autores = autores;
        this.generos = generos;
    }
}
