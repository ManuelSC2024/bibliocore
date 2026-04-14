package com.example.bibliocore.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "generos")
@Data // Esto es de Lombok crea los getters y setters automáticamente
public class Genero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Solo se muestra en JSON, no se puede modificar desde la solicitud
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String nombre;

    // Constructor vacío necesario para JPA
    public Genero() {
    }
}