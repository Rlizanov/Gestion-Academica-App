package com.cibertec.gestionacademicaapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioRequestDTO {

    @NotNull(message = "El curso es obligatorio")
    private Integer idCurso;

    @NotNull(message = "El docente es obligatorio")
    private Integer idDocente;

    @NotNull(message = "El ciclo académico es obligatorio")
    private Integer idCiclo;

    @NotBlank(message = "El día de la semana es obligatorio")
    private String diaSemana;

    @NotNull(message = "La hora de inicio es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    private String aula; // Es opcional según tu script
}