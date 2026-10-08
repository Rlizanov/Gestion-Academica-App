package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.MatriculaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.MatriculaResponseDTO;
import com.cibertec.gestionacademicaapp.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> registrar(@Valid @RequestBody MatriculaRequestDTO requestDTO) {
        MatriculaResponseDTO response = matriculaService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> listar() {
        return ResponseEntity.ok(matriculaService.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody MatriculaRequestDTO requestDTO) {
        return ResponseEntity.ok(matriculaService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        matriculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}