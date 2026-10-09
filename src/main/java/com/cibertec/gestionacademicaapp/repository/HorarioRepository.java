package com.cibertec.gestionacademicaapp.repository;

import com.cibertec.gestionacademicaapp.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    List<Horario> findByCicloAcademico_IdCiclo(Integer idCiclo);

    // Busca las clases del día para los cursos en los que está matriculado el alumno
    List<Horario> findByCicloAcademico_IdCicloAndDiaSemanaAndCurso_IdCursoInOrderByHoraInicioAsc(
            Integer idCiclo, String diaSemana, List<Integer> idCursos);
}