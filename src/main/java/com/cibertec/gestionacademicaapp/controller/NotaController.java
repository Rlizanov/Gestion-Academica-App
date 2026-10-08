package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.NotaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.NotaResponseDTO;
import com.cibertec.gestionacademicaapp.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @PostMapping
    public ResponseEntity<NotaResponseDTO> registrar(@Valid @RequestBody NotaRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.registrar(requestDTO));
    }

    @GetMapping("/detalle/{idDetalleMatricula}")
    public ResponseEntity<List<NotaResponseDTO>> listarPorDetalle(@PathVariable Integer idDetalleMatricula) {
        return ResponseEntity.ok(notaService.listarPorDetalle(idDetalleMatricula));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(notaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody NotaRequestDTO requestDTO) {
        return ResponseEntity.ok(notaService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        notaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}