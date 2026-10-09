package com.cibertec.gestionacademicaapp.controller;

import com.cibertec.gestionacademicaapp.dto.reporte.ClaseAgendaDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.SimuladorNotaDTO;
import com.cibertec.gestionacademicaapp.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // GET: http://localhost:8080/api/v1/reportes/agenda/alumno/1
    @GetMapping("/agenda/alumno/{idAlumno}")
    public ResponseEntity<List<ClaseAgendaDTO>> obtenerAgendaHoy(@PathVariable Integer idAlumno) {
        return ResponseEntity.ok(reporteService.obtenerAgendaHoyAlumno(idAlumno));
    }

    // GET: http://localhost:8080/api/v1/reportes/simulador/alumno/1/curso/2
    @GetMapping("/simulador/alumno/{idAlumno}/curso/{idCurso}")
    public ResponseEntity<SimuladorNotaDTO> simularNotas(
            @PathVariable Integer idAlumno,
            @PathVariable Integer idCurso) {
        return ResponseEntity.ok(reporteService.simularAprobacion(idAlumno, idCurso));
    }
}