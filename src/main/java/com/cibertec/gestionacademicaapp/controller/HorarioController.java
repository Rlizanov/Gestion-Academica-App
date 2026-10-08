package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.HorarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.HorarioResponseDTO;
import com.cibertec.gestionacademicaapp.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    @PostMapping
    public ResponseEntity<HorarioResponseDTO> registrar(@Valid @RequestBody HorarioRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioService.registrar(requestDTO));
    }

    @GetMapping("/ciclo/{idCiclo}")
    public ResponseEntity<List<HorarioResponseDTO>> listarPorCiclo(@PathVariable Integer idCiclo) {
        return ResponseEntity.ok(horarioService.listarPorCiclo(idCiclo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(horarioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody HorarioRequestDTO requestDTO) {
        return ResponseEntity.ok(horarioService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}