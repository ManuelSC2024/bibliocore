package com.example.bibliocore.modelo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "listaDeseados")
@Data // Esto es de Lombok crea los getters y setters automáticamente
public class ListaDeseados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Usuario usuario;

    @ManyToMany
    @JoinTable(name = "lista_deseados_libros", joinColumns = @JoinColumn(name = "lista_deseados_id"), inverseJoinColumns = @JoinColumn(name = "libro_id"))
    private List<Libro> libros = new ArrayList<>();

    public ListaDeseados() {
    }

    public ListaDeseados(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Agrega un libro a la lista de deseados si no está, o lo quita si ya está.
     * Devuelve true si se agregó, false si se quitó.
     *
     * @param libro El libro que se desea agregar o quitar de la lista de deseados.
     * @return true si el libro fue agregado a la lista de deseados, false si el
     *         libro fue eliminado de la lista de deseados.
     */
    public boolean agregarOQuitarLibro(Libro libro) {
        Boolean resutado;

        if (!libros.contains(libro)) {
            libros.add(libro);
            resutado = true;
        } else {
            libros.remove(libro);
            resutado = false;
        }

        return resutado;
    }
}
