package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByEstado(Boolean estado); // Para listar solo los activos
}