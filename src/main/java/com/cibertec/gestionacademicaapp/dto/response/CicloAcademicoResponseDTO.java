package com.cibertec.gestionacademicaapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CicloAcademicoResponseDTO {
    private Integer idCiclo;
    private String nombreCiclo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaInicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaFin;

    private Boolean estado;
}