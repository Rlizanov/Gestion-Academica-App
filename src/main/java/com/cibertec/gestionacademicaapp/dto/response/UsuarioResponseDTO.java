package com.cibertec.gestionacademicaapp.dto.response;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String usuario;
    private String rol;
    private Boolean estado;
    private Integer idAlumno;  // Útil para la app móvil para saber a qué alumno pertenece
    private Integer idDocente; // Útil para saber a qué docente pertenece
}