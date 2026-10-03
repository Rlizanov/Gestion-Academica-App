package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    Optional<Alumno> findByDni(String dni);
    Optional<Alumno> findByCorreo(String correo);
    List<Alumno> findByEstado(Boolean estado);

    List<Alumno> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);
}