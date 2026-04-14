package com.example.bibliocore.dto;

public class SolicitarPrestamoResponseDTO {
    private String mensaje;

    public SolicitarPrestamoResponseDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}