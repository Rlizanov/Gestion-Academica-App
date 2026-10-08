package com.cibertec.gestionacademicaapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AsistenciaRequestDTO {

    @NotNull(message = "El detalle de la matrícula es obligatorio")
    private Integer idDetalleMatricula;

    @NotNull(message = "La fecha de asistencia es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(PRESENTE|FALTA|TARDANZA)$", message = "El estado debe ser PRESENTE, FALTA o TARDANZA")
    private String estado;

    private String observacion;
}