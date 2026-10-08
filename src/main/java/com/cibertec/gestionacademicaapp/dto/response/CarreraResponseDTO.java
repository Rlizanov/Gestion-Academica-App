package com.cibertec.gestionacademicaapp.dto.response;

import lombok.Data;

@Data
public class CarreraResponseDTO {
    private Integer idCarrera;
    private String nombreCarrera;
    private Integer duracionSemestres;
    private Boolean estado;
}