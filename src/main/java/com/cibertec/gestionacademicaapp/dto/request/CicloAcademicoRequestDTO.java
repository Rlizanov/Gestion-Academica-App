package com.cibertec.gestionacademicaapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CicloAcademicoRequestDTO {

    @NotBlank(message = "El nombre del ciclo es obligatorio")
    private String nombreCiclo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;
}