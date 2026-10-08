package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.DocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.DocenteResponseDTO;
import com.cibertec.gestionacademicaapp.service.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService docenteService;

    @GetMapping
    public ResponseEntity<List<DocenteResponseDTO>> listar() {
        return ResponseEntity.ok(docenteService.listarActivos());
    }

    @PostMapping
    public ResponseEntity<DocenteResponseDTO> registrar(@Valid @RequestBody DocenteRequestDTO requestDTO) {
        DocenteResponseDTO response = docenteService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody DocenteRequestDTO requestDTO) {
        return ResponseEntity.ok(docenteService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        docenteService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }
}