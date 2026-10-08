package com.cibertec.gestionacademicaapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CursoRequestDTO {

    @NotBlank(message = "El nombre del curso es obligatorio")
    private String nombreCurso;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "El curso debe tener al menos 1 crédito")
    private Integer creditos;

    @NotNull(message = "Las horas semanales son obligatorias")
    @Min(value = 1, message = "El curso debe tener al menos 1 hora semanal")
    private Integer horasSemanales;
}