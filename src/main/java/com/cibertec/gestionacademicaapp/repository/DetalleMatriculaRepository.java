package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.DetalleMatricula;
import com.cibertec.gestionacademicaapp.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula, Integer> {
    List<DetalleMatricula> findByMatricula_IdMatricula(Integer idMatricula);

}