package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "USUARIO")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer idUsuario; // PK

    @Column(name = "IdAlumno")
    private Integer idAlumno; // FK

    @Column(name = "IdDocente")
    private Integer idDocente; // FK[cite: 3]

    @Column(name = "Usuario")
    private String usuario; // varchar(50)[cite: 3]

    @Column(name = "PasswordHash")
    private String passwordHash; // varchar(255)[cite: 3]

    @Column(name = "Rol")
    private String rol; // varchar(20)[cite: 3]

    @Column(name = "Estado")
    private Boolean estado; // bit[cite: 3]
}