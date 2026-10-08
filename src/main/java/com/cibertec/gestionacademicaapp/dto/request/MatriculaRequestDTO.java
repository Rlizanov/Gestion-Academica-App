package com.cibertec.gestionacademicaapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class MatriculaRequestDTO {

    @NotNull(message = "El alumno es obligatorio")
    private Integer idAlumno;

    @NotNull(message = "El ciclo es obligatorio")
    private Integer idCiclo;

    @NotEmpty(message = "Debe matricularse en al menos un curso")
    private List<Integer> cursosIds; // Lista de IDs de los cursos elegidos
}