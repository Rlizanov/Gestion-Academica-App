package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.SueldoDocente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SueldoDocenteRepository extends JpaRepository<SueldoDocente, Integer> {
    List<SueldoDocente> findByDocente_IdDocenteAndEstadoTrue(Integer idDocente);
}