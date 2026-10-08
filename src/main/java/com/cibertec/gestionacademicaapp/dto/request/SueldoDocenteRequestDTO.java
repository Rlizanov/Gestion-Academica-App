package com.cibertec.gestionacademicaapp.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SueldoDocenteRequestDTO {

    @NotNull(message = "El docente es obligatorio")
    private Integer idDocente;

    @NotNull(message = "Las horas trabajadas son obligatorias")
    @Min(value = 1, message = "Debe haber trabajado al menos 1 hora")
    private Integer horasTrabajadas;

    @NotNull(message = "El pago por hora es obligatorio")
    @Min(value = 1, message = "El pago mínimo por hora debe ser mayor a 0")
    private BigDecimal pagoPorHora;

    @NotNull(message = "La fecha de pago es obligatoria")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaPago;
}