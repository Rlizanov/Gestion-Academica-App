package com.cibertec.gestionacademicaapp.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AlumnoResponseDTO {

    private Integer idAlumno;
    private String nombres;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String correo;
    private String telefono;
    private Boolean estado;

    // El verdadero poder del DTO: aplanar la información
    private String nombreCarrera;
}