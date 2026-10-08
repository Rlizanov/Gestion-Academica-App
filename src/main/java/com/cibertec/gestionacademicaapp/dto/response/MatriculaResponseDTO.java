package com.cibertec.gestionacademicaapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatriculaResponseDTO {
    private Integer idMatricula;
    private Integer idAlumno;
    private Integer idCiclo;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaRegistro;

    private String estado;
    private List<DetalleMatriculaResponseDTO> detalles;
}