package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.AuthRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AuthResponseDTO;
import com.cibertec.gestionacademicaapp.dto.request.UsuarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.UsuarioResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Usuario;
import com.cibertec.gestionacademicaapp.repository.UsuarioRepository;
import com.cibertec.gestionacademicaapp.security.JwtService;
import com.cibertec.gestionacademicaapp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    // Inyectamos tu servicio que ya tenías
    private final UsuarioService usuarioService;

    // 1. EL LOGIN (Genera el Token)
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {

        // Esto reemplaza tu antiguo método "autenticar".
        // Spring Security busca al usuario y hace el "passwordEncoder.matches" por ti.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getPassword())
        );

        // Si pasó la línea anterior, la contraseña es correcta. Buscamos al usuario para sacar su Rol.
        Usuario user = usuarioRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Fabricamos el Token JWT
        String jwtToken = jwtService.generateToken(user.getUsername());

        // Devolvemos el Token y el Rol a la app móvil
        return ResponseEntity.ok(AuthResponseDTO.builder()
                .token(jwtToken)
                .rol(user.getRol())
                .build());
    }

    // 2. EL REGISTRO (Usa tu código intacto)
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        // Llamamos a tu método registrar tal cual lo tenías programado
        UsuarioResponseDTO response = usuarioService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}