package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "HORARIO")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdHorario")
    private Integer idHorario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCurso", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCiclo", nullable = false)
    private CicloAcademico cicloAcademico;

    @Column(name = "DiaSemana", length = 15, nullable = false)
    private String diaSemana;

    @Column(name = "HoraInicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "HoraFin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "Aula", length = 20)
    private String aula;
}