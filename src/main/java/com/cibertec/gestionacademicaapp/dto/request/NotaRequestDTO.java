package com.cibertec.gestionacademicaapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class NotaRequestDTO {

    @NotNull(message = "El detalle de la matrícula es obligatorio")
    private Integer idDetalleMatricula;

    @NotNull(message = "La nota es obligatoria")
    @Min(value = 0, message = "La nota mínima es 0")
    @Max(value = 20, message = "La nota máxima es 20")
    private Double nota;

    @NotBlank(message = "El tipo de evaluación es obligatorio")
    private String tipoEvaluacion;

    @NotNull(message = "La fecha de evaluación es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaEvaluacion;
}