package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    List<Docente> findByEstado(Boolean estado);
}