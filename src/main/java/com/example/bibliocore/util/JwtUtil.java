package com.example.bibliocore.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.bibliocore.modelo.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long jwtExpirationMs;

    /**
     * Inicializa la clave de firma y el tiempo de expiración del JWT.
     *
     * @param secret          secreto usado para firmar tokens.
     * @param jwtExpirationMs duración del token en milisegundos.
     */
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpirationMs) {
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalArgumentException("jwt.secret debe tener al menos 32 bytes para HS256");
        }
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /**
     * Genera un token JWT firmado para un usuario.
     * Incluye el rol y el nombre en los claims del token.
     *
     * @param usuario usuario autenticado.
     * @return token JWT.
     */
    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", usuario.isAdmin() ? "ADMIN" : "USER");
        claims.put("nombre", usuario.getNombre());

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * Extrae el username desde un token JWT.
     *
     * @param token token JWT.
     * @return username contenido en el token.
     */
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Valida que el token pertenezca al usuario y no esté expirado.
     *
     * @param token    token JWT.
     * @param username username esperado.
     * @return true si el token es válido.
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Comprueba si un token JWT está expirado.
     *
     * @param token token JWT.
     * @return true si el token ya expiró.
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Extrae el rol almacenado en el token JWT.
     *
     * @param token token JWT.
     * @return rol del usuario (ADMIN o USER).
     */
    public String extraerRoles(String token) {
        return (String) Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload()
                .get("role");
    }
}
