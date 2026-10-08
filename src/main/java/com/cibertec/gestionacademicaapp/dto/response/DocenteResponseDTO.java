package com.cibertec.gestionacademicaapp.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DocenteResponseDTO {
    private Integer idDocente;
    private String nombres;
    private String apellidos;
    private String dni;
    private String correo;
    private String telefono;
    private String especialidad;
    private LocalDate fechaIngreso;
    private Boolean estado;
}
