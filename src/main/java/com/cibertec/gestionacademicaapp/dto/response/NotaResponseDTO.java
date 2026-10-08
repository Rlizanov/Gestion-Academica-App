package com.cibertec.gestionacademicaapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NotaResponseDTO {
    private Integer idNota;
    private Integer idDetalleMatricula;
    private String nombreCurso;
    private Double nota;
    private String tipoEvaluacion;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaEvaluacion;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaRegistro;
}