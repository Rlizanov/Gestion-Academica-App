package com.cibertec.gestionacademicaapp.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SueldoDocenteResponseDTO {
    private Integer idSueldo;
    private Integer idDocente;
    private String nombreCompletoDocente;
    private Integer horasTrabajadas;
    private BigDecimal pagoPorHora;
    private BigDecimal sueldoTotal;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaPago;

    private Boolean estado;
}