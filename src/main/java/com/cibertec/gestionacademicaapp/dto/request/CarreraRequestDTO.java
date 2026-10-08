package com.cibertec.gestionacademicaapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarreraRequestDTO {

    @NotBlank(message = "El nombre de la carrera es obligatorio")
    private String nombreCarrera;

    @NotNull(message = "La duración en semestres es obligatoria")
    @Min(value = 1, message = "La duración mínima es de 1 semestre")
    private Integer duracionSemestres;
}