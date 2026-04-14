package com.example.bibliocore.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.bibliocore.util.JwtRequestFilter;

@Configuration
@EnableWebSecurity // Habilita la seguridad web
@EnableMethodSecurity // Habilita la seguridad a nivel de método (con @PreAuthorize)
public class SecurityConfig {

    // Inyectamos el filtro de JWT para que se ejecute en cada solicitud
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Crea una instancia de BCryptPasswordEncoder para encriptar las contraseñas.
     * 
     * @return Una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura las reglas de seguridad HTTP. Por ahora, deshabilitamos CSRF y
     * permitimos todas las solicitudes.
     * 
     * @param http El objeto HttpSecurity que se utiliza para configurar la
     *             seguridad HTTP.
     * @return Una instancia de SecurityFilterChain que define las reglas de
     *         seguridad para las solicitudes HTTP.
     * @throws Exception Si ocurre un error durante la configuración de la seguridad
     *                   HTTP.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configuramos las reglas de seguridad HTTP
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) /// No usamos sesiones, cada solicitud
                                                                                /// debe ser autenticada con un token
                )
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (login y registro)
                        .requestMatchers("/api/usuarios/login", "/api/usuarios/registrarUsuario").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Catálogo de libros público (Solo lectura)
                        .requestMatchers(HttpMethod.GET, "/api/libros/**").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/api/prestamos/libroDisponible/{libroId}").permitAll()
                        // Rutas de portadas (permitimos acceso público a las imágenes)
                        .requestMatchers("/portadas/**").permitAll()

                        // Todo lo demás (Préstamos, Crear, etc.) requiere Token
                        .anyRequest().authenticated());

        // Agregamos el filtro de JWT antes del filtro de autenticación de Spring
        // Security
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
