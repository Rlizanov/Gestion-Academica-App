package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ALUMNO")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAlumno")
    private Integer idAlumno;

    @Column(name = "Nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "Apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "DNI", length = 8, nullable = false, unique = true)
    private String dni;

    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "Correo", length = 100, nullable = false, unique = true)
    private String correo;

    @Column(name = "Telefono", length = 15)
    private String telefono;

    // Relación con la tabla Carrera
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdCarrera", nullable = false)
    private Carrera carrera;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}