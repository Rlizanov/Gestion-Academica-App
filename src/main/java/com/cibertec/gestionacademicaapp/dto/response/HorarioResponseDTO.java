package com.cibertec.gestionacademicaapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioResponseDTO {
    private Integer idHorario;
    private String nombreCurso;
    private String nombreDocente;
    private String nombreCiclo;
    private String diaSemana;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    private String aula;
}