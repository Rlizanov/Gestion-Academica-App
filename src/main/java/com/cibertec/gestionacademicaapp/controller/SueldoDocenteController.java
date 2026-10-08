package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.SueldoDocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.SueldoDocenteResponseDTO;
import com.cibertec.gestionacademicaapp.service.SueldoDocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sueldos")
@RequiredArgsConstructor
public class SueldoDocenteController {

    private final SueldoDocenteService sueldoService;

    @PostMapping
    public ResponseEntity<SueldoDocenteResponseDTO> registrar(@Valid @RequestBody SueldoDocenteRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sueldoService.registrar(requestDTO));
    }

    @GetMapping("/docente/{idDocente}")
    public ResponseEntity<List<SueldoDocenteResponseDTO>> listarPorDocente(@PathVariable Integer idDocente) {
        return ResponseEntity.ok(sueldoService.listarPorDocente(idDocente));
    }
    @GetMapping("/{id}")
    public ResponseEntity<SueldoDocenteResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(sueldoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SueldoDocenteResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody SueldoDocenteRequestDTO requestDTO) {
        return ResponseEntity.ok(sueldoService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        sueldoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}