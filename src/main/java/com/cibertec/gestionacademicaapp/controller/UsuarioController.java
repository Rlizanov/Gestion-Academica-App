package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.UsuarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.UsuarioResponseDTO;
import com.cibertec.gestionacademicaapp.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // POST: http://localhost:8080/api/v1/usuarios/login
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO response = usuarioService.autenticar(requestDTO);
        return ResponseEntity.ok(response); // Devuelve un 200 OK si todo está correcto
    }


    // POST: http://localhost:8080/api/v1/usuarios/registro
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO response = usuarioService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}