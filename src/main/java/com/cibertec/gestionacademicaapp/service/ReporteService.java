package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;

public interface ReporteService {
    DashboardAlumnoResponseDTO obtenerDashboardAlumno(Integer idAlumno);
}