package com.example.bibliocore.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// Orden personalizado de los campos en JSON
@JsonPropertyOrder({ "id", "username", "password", "email", "nombre", "sancionado", "finSancion" })
@Entity
@Table(name = "usuarios")
@Data // Esto es de Lombok crea los getters y setters automáticamente
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Solo se muestra en JSON, no se puede modificar desde la solicitud
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    // Solo se puede enviar en la solicitud, no se muestra en JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; // Nota: No guardar en texto plano

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean activo = true;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean admin = false;

    // Sanciones
    // Solo se muestra en JSON, no se puede modificar desde la solicitud
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean sancionado = false;
    // Solo se muestra en JSON, no se puede modificar desde la solicitud
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate finSancion = null;

    // Un usuario puede tener muchos hasta 3 prestamos.
    @JsonIgnore // Evita la recursividad infinita al convertir a JSON
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos;

    // Constructor vacío necesario para JPA
    public Usuario() {
    }

    /**
     * Constructor para crear un nuevo usuario. La contraseña se encripta
     * automáticamente al crear el usuario.
     * 
     * @param username El nombre de usuario único para el usuario.
     * @param password La contraseña del usuario, que se encriptará antes de
     *                 guardarse en la base de datos.
     * @param nombre   El nombre completo del usuario.
     * @param email    La dirección de correo electrónico del usuario.
     */
    public Usuario(String username, String password, String nombre, String email) {
        this.username = username;
        this.password = password; // Encriptar la contraseña al crear el usuario
        this.nombre = nombre;
        this.email = email;
    }

    /**
     * Metodo para verificar si el usuario puede pedir un préstamo.
     * 
     * @return true si el usuario puede pedir un préstamo, false en caso contrario.
     */
    public boolean puedePedirPrestado() {
        boolean puedePedirPrestado = false;

        if (sancionado && finSancion != null) {
            if (LocalDate.now().isAfter(finSancion)) {
                this.sancionado = false;
                this.finSancion = null;
                puedePedirPrestado = true;
            } else {
                puedePedirPrestado = false;
            }
        } else {
            puedePedirPrestado = true;
        }
        return puedePedirPrestado;
    }

}
