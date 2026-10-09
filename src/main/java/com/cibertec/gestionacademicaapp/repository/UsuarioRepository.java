package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {


    Optional<Usuario> findByUsuarioAndEstado(String usuario, Boolean estado);

    // Método clave para que Spring Security busque al usuario al hacer Login
    Optional<Usuario> findByUsuario(String usuario);
}