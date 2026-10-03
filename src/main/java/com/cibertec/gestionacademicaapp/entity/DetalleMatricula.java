package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DETALLE_MATRICULA")
public class DetalleMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetalleMatricula")
    private Integer idDetalleMatricula;

    // Relación con Matricula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdMatricula", nullable = false)
    private Matricula matricula;

    // Relación con Curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCurso", nullable = false)
    private Curso curso;
}