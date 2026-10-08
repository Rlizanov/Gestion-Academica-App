package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    List<Matricula> findByEstado(String estado);

    // AQUÍ ES DONDE DEBE IR ESTE MÉTODO (porque Matricula sí tiene Alumno y Estado)
    Optional<Matricula> findByAlumno_IdAlumnoAndEstado(Integer idAlumno, String estado);
}