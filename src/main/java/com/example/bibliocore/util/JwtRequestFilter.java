package com.example.bibliocore.util;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Sacar el token de la cabecera Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // El token debe empezar por "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                // Usamos el JwtUtil para sacar el nombre del usuario del token
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("Token inválido ou caducado");
            }
        }

        // Si tenemos un nombre de usuario y no hay nadie autenticado en el contexto de
        // seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Validamos si el token es correcto
            if (jwtUtil.validateToken(jwt, username)) {
                String rol = jwtUtil.extraerRoles(jwt);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));

                // Creamos la "identidad" del usuario para Spring
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                // Añadimos detalles de la solicitud al token de autenticación
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Le decimos a Spring que el usuario está autenticado
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continuar con la petición (ir al controlador)
        chain.doFilter(request, response);
    }
}