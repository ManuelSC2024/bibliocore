package com.example.bibliocore.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

// Orden personalizado de los campos en JSON
@JsonPropertyOrder({ "id", "usuario", "libro", "fechaInicioPrestamo", "fechaDevolucionPrevista", "fechaDevolucion" })
@Entity
@Table(name = "prestamos")
@Data
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Solo se muestra en JSON, no se puede modificar desde la solicitud
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    // Local date solo tiene dia mes y años. Formato: YYYY-MM-DD
    @Column(nullable = false)
    private LocalDate fechaInicioPrestamo;

    @Column(nullable = false)
    private LocalDate fechaDevolucionPrevista;

    private LocalDate fechaDevolucion; // Puede ser null hasta que lo devuelvan

    // Relaciones
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    // Constructor vacío necesario para JPA
    public Prestamo() {
    }

    /**
     * Constructor para crear un nuevo préstamo. Al crear el préstamo se calcula
     * automáticamente la fecha de inicio y la fecha de devolución prevista.
     * 
     * @param usuario
     * @param libro
     */
    public Prestamo(Usuario usuario, Libro libro) {
        this.usuario = usuario;
        this.libro = libro;
        this.iniciarPrestamo();
    }

    /**
     * Metodo que calcula la fecha de devolucion del prestamo.
     */
    public void iniciarPrestamo() {
        this.fechaInicioPrestamo = LocalDate.now();
        this.fechaDevolucionPrevista = this.fechaInicioPrestamo.plusDays(15);
    }

    /**
     * Metodo para procesar la devolución del libro. Calcula si el libro se devuelve
     * tarde y aplica sanciones al usuario en caso de retraso.
     */
    public void devolucion() {
        this.fechaDevolucion = LocalDate.now();

        if (this.fechaDevolucion.isAfter(this.fechaDevolucionPrevista)) {
            long diasRetraso = ChronoUnit.DAYS.between(this.fechaDevolucionPrevista, this.fechaDevolucion);
            long diasSancion = diasRetraso * 3;

            this.usuario.setSancionado(true);
            this.usuario.setFinSancion(LocalDate.now().plusDays(diasSancion));
        }
    }

}
