package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    // GET: http://localhost:8080/api/v1/reportes/dashboard/alumno/1
    @GetMapping("/dashboard/alumno/{idAlumno}")
    public ResponseEntity<DashboardAlumnoResponseDTO> obtenerDashboard(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(reporteService.obtenerDashboardAlumno(idAlumno));
    }
}