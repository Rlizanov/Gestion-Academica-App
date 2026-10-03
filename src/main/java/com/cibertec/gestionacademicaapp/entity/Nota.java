package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "NOTA")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdNota")
    private Integer idNota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDetalleMatricula", nullable = false)
    private DetalleMatricula detalleMatricula;

    @Column(name = "Nota", precision = 4, scale = 2, nullable = false)
    private BigDecimal nota;

    @Column(name = "TipoEvaluacion", length = 30, nullable = false)
    private String tipoEvaluacion;

    @Column(name = "FechaEvaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(name = "FechaRegistro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;
}