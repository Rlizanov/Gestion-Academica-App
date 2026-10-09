package com.cibertec.gestionacademicaapp.dto.reporte;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SimuladorNotaDTO {
    private String nombreCurso;
    private int notasRegistradas;
    private int notasFaltantes;
    private BigDecimal promedioActual;
    private BigDecimal notaMinimaRequerida; // Lo que debe sacar en cada examen restante
    private String mensajeAlerta;
    private String colorSemaforo; // VERDE, AMARILLO, ROJO
}