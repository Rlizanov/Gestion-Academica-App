package com.cibertec.gestionacademicaapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Esta es tu "Firma Digital". Es una llave secreta encriptada en Base64.
    // ¡En la vida real esto va en variables de entorno, pero aquí la dejaremos para facilitar las pruebas!
    private static final String SECRET_KEY = "Q2liZXJ0ZWNHZXN0aW9uQWNhZGVtaWNhQXBwU2VjcmV0S2V5MTIzNDU2Nzg5MA==";

    // 1. Extraer el nombre de usuario (email o login) del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Generar un Token nuevo (solo con el usuario)
    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    // 3. Generar un Token nuevo pasándole datos extra (Roles, IDs, etc)
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // El token dura 24 horas (1000 ms * 60 s * 60 min * 24 h)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 4. Validar si el token es correcto y pertenece al usuario
    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Traduce tu SECRET_KEY a una llave criptográfica real
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}