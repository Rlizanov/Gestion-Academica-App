package com.cibertec.gestionacademicaapp.dto.reporte;

import lombok.Data;
import java.util.List;

@Data
public class DashboardAlumnoResponseDTO {
    private Integer idAlumno;
    private String nombreCompleto;
    private String nombreCarrera;
    private String cicloActual;

    // Lista con el resumen de todos sus cursos
    private List<CursoRendimientoDTO> cursos;
}