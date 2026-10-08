package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.AsistenciaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AsistenciaResponseDTO;
import com.cibertec.gestionacademicaapp.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> registrar(@Valid @RequestBody AsistenciaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.registrar(requestDTO));
    }

    @GetMapping("/detalle/{idDetalleMatricula}")
    public ResponseEntity<List<AsistenciaResponseDTO>> listarPorDetalle(@PathVariable Integer idDetalleMatricula) {
        return ResponseEntity.ok(asistenciaService.listarPorDetalle(idDetalleMatricula));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody AsistenciaRequestDTO requestDTO) {
        return ResponseEntity.ok(asistenciaService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}