package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer idUsuario;

    // Relación opcional con Alumno (Puede ser nulo por tu diseño de arcos exclusivos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno")
    private Alumno alumno;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente")
    private Docente docente;

    @Column(name = "Usuario", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PasswordHash", length = 255, nullable = false)
    private String password;

    @Column(name = "Rol", length = 20, nullable = false)
    private String rol;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;
}