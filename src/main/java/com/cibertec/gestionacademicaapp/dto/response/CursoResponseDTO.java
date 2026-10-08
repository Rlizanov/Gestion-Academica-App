package com.cibertec.gestionacademicaapp.dto.response;

import lombok.Data;

@Data
public class CursoResponseDTO {
    private Integer idCurso;
    private String nombreCurso;
    private Integer creditos;
    private Integer horasSemanales;
    private Boolean estado;
}