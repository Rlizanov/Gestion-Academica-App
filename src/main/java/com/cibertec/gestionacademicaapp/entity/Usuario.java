package com.cibertec.gestionacademicaapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "USUARIO")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno")
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdDocente")
    private Docente docente;

    @Column(name = "Usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name = "Rol", nullable = false)
    private String rol;

    @Column(name = "Estado", nullable = false)
    private Boolean estado = true;

    // ==============================================================
    // MÉTODOS OBLIGATORIOS DE SPRING SECURITY (UserDetails)
    // ==============================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Le agregamos el prefijo "ROLE_" porque Spring lo exige por convención interna
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {
        return this.passwordHash; // Le decimos a Spring dónde está la contraseña
    }

    @Override
    public String getUsername() {
        return this.usuario; // Le decimos a Spring dónde está el login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.estado; // Si el estado es false, la cuenta se bloquea
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.estado; // Solo permite login si el estado es true (1)
    }
}