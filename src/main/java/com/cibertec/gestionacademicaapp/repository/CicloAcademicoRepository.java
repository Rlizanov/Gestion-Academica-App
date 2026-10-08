package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.CicloAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CicloAcademicoRepository extends JpaRepository<CicloAcademico, Integer> {
    List<CicloAcademico> findByEstado(Boolean estado);
}