package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    List<Asistencia> findByDetalleMatricula_IdDetalleMatricula(Integer idDetalleMatricula);
}