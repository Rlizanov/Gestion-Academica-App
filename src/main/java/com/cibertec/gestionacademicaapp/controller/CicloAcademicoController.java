package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.CicloAcademicoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CicloAcademicoResponseDTO;
import com.cibertec.gestionacademicaapp.service.CicloAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ciclos")
@RequiredArgsConstructor
public class CicloAcademicoController {

    private final CicloAcademicoService cicloService;

    @GetMapping
    public ResponseEntity<List<CicloAcademicoResponseDTO>> listar() {
        return ResponseEntity.ok(cicloService.listarActivos());
    }

    @PostMapping
    public ResponseEntity<CicloAcademicoResponseDTO> registrar(@Valid @RequestBody CicloAcademicoRequestDTO requestDTO) {
        CicloAcademicoResponseDTO response = cicloService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CicloAcademicoResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CicloAcademicoRequestDTO requestDTO) {
        return ResponseEntity.ok(cicloService.actualizar(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        cicloService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }
}