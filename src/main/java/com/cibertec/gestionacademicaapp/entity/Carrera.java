package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CARRERA")
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCarrera")
    private Integer idCarrera;

    @Column(name = "NombreCarrera", length = 100, nullable = false)
    private String nombreCarrera;

    @Column(name = "DuracionSemestres", nullable = false)
    private Integer duracionSemestres;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}
