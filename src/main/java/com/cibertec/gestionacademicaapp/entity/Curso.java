package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CURSO")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCurso")
    private Integer idCurso;

    @Column(name = "NombreCurso", length = 100, nullable = false)
    private String nombreCurso;

    @Column(name = "Creditos", nullable = false)
    private Integer creditos;

    @Column(name = "HorasSemanales", nullable = false)
    private Integer horasSemanales;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}