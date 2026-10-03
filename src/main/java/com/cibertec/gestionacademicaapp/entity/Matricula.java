package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MATRICULA")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMatricula")
    private Integer idMatricula;

    // Relación con Alumno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno", nullable = false)
    private Alumno alumno;

    // Relación con Ciclo Académico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCiclo", nullable = false)
    private CicloAcademico cicloAcademico;

    // insertable = false delega la creación de la fecha a SQL Server (GETDATE)
    @Column(name = "FechaRegistro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado = "ACTIVA";
}