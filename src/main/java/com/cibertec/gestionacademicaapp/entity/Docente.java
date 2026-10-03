package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "DOCENTE")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDocente")
    private Integer idDocente;

    @Column(name = "Nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "Apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "DNI", length = 8, nullable = false, unique = true)
    private String dni;

    @Column(name = "Correo", length = 100, nullable = false, unique = true)
    private String correo;

    @Column(name = "Telefono", length = 15)
    private String telefono;

    @Column(name = "Especialidad", length = 100)
    private String especialidad;

    @Column(name = "FechaIngreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}