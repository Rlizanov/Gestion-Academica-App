package com.cibertec.gestionacademicaapp.dto.reporte;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CursoRendimientoDTO {
    private String nombreCurso;
    private BigDecimal promedioNotas;
    private Integer cantidadFaltas;

    // Aquí viaja la lógica de negocio procesada para la App
    private String mensajeAlerta;
    private String colorSemaforo; // Valores: ROJO, AMARILLO, VERDE, GRIS
}