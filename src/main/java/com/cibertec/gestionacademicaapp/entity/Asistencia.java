package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ASISTENCIA")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAsistencia")
    private Integer idAsistencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDetalleMatricula", nullable = false)
    private DetalleMatricula detalleMatricula;

    @Column(name = "Fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "FechaRegistro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "Observacion", length = 200)
    private String observacion;
}