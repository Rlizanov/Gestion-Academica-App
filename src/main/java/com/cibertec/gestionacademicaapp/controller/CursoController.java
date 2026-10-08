package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.CursoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CursoResponseDTO;
import com.cibertec.gestionacademicaapp.service.CursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listar() {
        return ResponseEntity.ok(cursoService.listarActivos());
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> registrar(@Valid @RequestBody CursoRequestDTO requestDTO) {
        CursoResponseDTO response = cursoService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CursoRequestDTO requestDTO) {
        return ResponseEntity.ok(cursoService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cursoService.eliminarLogico(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}