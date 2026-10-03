package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.request.AlumnoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.service.AlumnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    // GET: http://localhost:8080/api/v1/alumnos
    @GetMapping
    public ResponseEntity<List<AlumnoResponseDTO>> listarAlumnos() {
        return ResponseEntity.ok(alumnoService.listarAlumnosActivos());
    }

    // GET: http://localhost:8080/api/v1/alumnos/1
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(alumnoService.obtenerPorId(id));
    }

    // POST: http://localhost:8080/api/v1/alumnos
    @PostMapping
    public ResponseEntity<AlumnoResponseDTO> registrarAlumno(@Valid @RequestBody AlumnoRequestDTO requestDTO) {
        AlumnoResponseDTO nuevoAlumno = alumnoService.registrarAlumno(requestDTO);
        return new ResponseEntity<>(nuevoAlumno, HttpStatus.CREATED);
    }

    // PUT: http://localhost:8080/api/v1/alumnos/1
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> actualizarAlumno(@PathVariable Integer id, @Valid @RequestBody AlumnoRequestDTO requestDTO) {
        return ResponseEntity.ok(alumnoService.actualizarAlumno(id, requestDTO));
    }

    // DELETE: http://localhost:8080/api/v1/alumnos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Integer id) {
        alumnoService.eliminarAlumno(id);
        return ResponseEntity.noContent().build();
    }
}