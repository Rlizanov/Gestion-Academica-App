package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "CICLO_ACADEMICO")
public class CicloAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCiclo")
    private Integer idCiclo;

    @Column(name = "NombreCiclo", length = 20, nullable = false)
    private String nombreCiclo;

    @Column(name = "FechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "FechaFin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}