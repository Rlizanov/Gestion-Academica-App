package com.cibertec.gestionacademicaapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AsistenciaResponseDTO {
    private Integer idAsistencia;
    private Integer idDetalleMatricula;
    private String nombreAlumno;
    private String nombreCurso;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;

    private String estado;
    private String observacion;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaRegistro;
}