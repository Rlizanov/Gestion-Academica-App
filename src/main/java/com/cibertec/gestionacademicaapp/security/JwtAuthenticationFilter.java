package com.cibertec.gestionacademicaapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Buscamos el Header llamado "Authorization" en la petición
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Si no hay Header o no empieza con "Bearer ", lo dejamos pasar sin autenticar
        // (Spring Security más adelante decidirá si lo bloquea por no estar autenticado)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token (cortando los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);

        // 4. Si el token tiene un usuario y no ha sido autenticado aún en este hilo
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Vamos a la base de datos a traer los detalles del usuario
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 5. Validamos matemáticamente que el token sea genuino y no haya caducado
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {

                // 6. Creamos la credencial de acceso oficial de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities() // Aquí van sus ROLES
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7. Le decimos a Spring: "Este usuario es legítimo, déjalo pasar"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Pasa a la siguiente puerta
        filterChain.doFilter(request, response);
    }
}