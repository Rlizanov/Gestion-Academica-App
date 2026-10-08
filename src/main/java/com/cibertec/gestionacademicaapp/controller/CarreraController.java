package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.CarreraRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CarreraResponseDTO;
import com.cibertec.gestionacademicaapp.service.CarreraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carreras")
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService carreraService;

    @PostMapping
    public ResponseEntity<CarreraResponseDTO> registrar(@Valid @RequestBody CarreraRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carreraService.registrar(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<CarreraResponseDTO>> listarActivas() {
        return ResponseEntity.ok(carreraService.listarActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(carreraService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CarreraRequestDTO requestDTO) {
        return ResponseEntity.ok(carreraService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        carreraService.eliminar(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}