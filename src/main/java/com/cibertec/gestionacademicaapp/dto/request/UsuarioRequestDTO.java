package com.cibertec.gestionacademicaapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String passwordHash;

    private String rol; // Ej: "ALUMNO" o "DOCENTE"
    private Integer idAlumno;
    private Integer idDocente;
}