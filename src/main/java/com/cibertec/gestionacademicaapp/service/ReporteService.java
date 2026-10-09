package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.reporte.ClaseAgendaDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.SimuladorNotaDTO;

import java.util.List;

public interface ReporteService {
    DashboardAlumnoResponseDTO obtenerDashboardAlumno(Integer idAlumno);
    List<ClaseAgendaDTO> obtenerAgendaHoyAlumno(Integer idAlumno);
    SimuladorNotaDTO simularAprobacion(Integer idAlumno, Integer idCurso);
}