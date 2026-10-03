package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SUELDO_DOCENTE")
public class SueldoDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdSueldo")
    private Integer idSueldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente", nullable = false)
    private Docente docente;

    @Column(name = "HorasTrabajadas", nullable = false)
    private Integer horasTrabajadas;

    @Column(name = "PagoPorHora", precision = 10, scale = 2, nullable = false)
    private BigDecimal pagoPorHora;

    @Column(name = "SueldoTotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal sueldoTotal;

    @Column(name = "FechaPago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}