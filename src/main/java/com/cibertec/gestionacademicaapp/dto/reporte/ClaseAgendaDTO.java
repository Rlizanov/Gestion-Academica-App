package com.cibertec.gestionacademicaapp.dto.reporte;

import lombok.Data;
import java.time.LocalTime;

@Data
public class ClaseAgendaDTO {
    private String nombreCurso;
    private String nombreDocente;
    private String aula;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estadoClase; // "FINALIZADA", "EN CURSO", "POR INICIAR"
}