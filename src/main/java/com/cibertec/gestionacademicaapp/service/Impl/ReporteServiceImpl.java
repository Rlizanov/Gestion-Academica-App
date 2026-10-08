package com.cibertec.gestionacademicaapp.service.Impl;


import com.cibertec.gestionacademicaapp.dto.reporte.CursoRendimientoDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Asistencia;
import com.cibertec.gestionacademicaapp.entity.DetalleMatricula;
import com.cibertec.gestionacademicaapp.entity.Matricula;
import com.cibertec.gestionacademicaapp.entity.Nota;
import com.cibertec.gestionacademicaapp.repository.AsistenciaRepository;
import com.cibertec.gestionacademicaapp.repository.DetalleMatriculaRepository;
import com.cibertec.gestionacademicaapp.repository.MatriculaRepository;
import com.cibertec.gestionacademicaapp.repository.NotaRepository;
import com.cibertec.gestionacademicaapp.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final MatriculaRepository matriculaRepository;
    private final DetalleMatriculaRepository detalleRepository;
    private final NotaRepository notaRepository;
    private final AsistenciaRepository asistenciaRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardAlumnoResponseDTO obtenerDashboardAlumno(Integer idAlumno) {

        // 1. Obtener la matrícula activa del alumno
        Matricula matricula = matriculaRepository.findByAlumno_IdAlumnoAndEstado(idAlumno, "ACTIVA")
                .orElseThrow(() -> new RuntimeException("El alumno no tiene una matrícula activa en este momento."));

        // 2. Armar la cabecera del perfil
        DashboardAlumnoResponseDTO dashboard = new DashboardAlumnoResponseDTO();
        dashboard.setIdAlumno(matricula.getAlumno().getIdAlumno());
        dashboard.setNombreCompleto(matricula.getAlumno().getNombres() + " " + matricula.getAlumno().getApellidos());
        dashboard.setNombreCarrera(matricula.getAlumno().getCarrera().getNombreCarrera());
        dashboard.setCicloActual(matricula.getCicloAcademico().getNombreCiclo());

        // 3. Obtener los cursos matriculados
        List<DetalleMatricula> detalles = detalleRepository.findByMatricula_IdMatricula(matricula.getIdMatricula());
        List<CursoRendimientoDTO> listaCursos = new ArrayList<>();

        for (DetalleMatricula detalle : detalles) {
            CursoRendimientoDTO cursoDTO = new CursoRendimientoDTO();
            cursoDTO.setNombreCurso(detalle.getCurso().getNombreCurso());

            // --- LÓGICA: Calcular Promedio de Notas ---
            List<Nota> notas = notaRepository.findByDetalleMatricula_IdDetalleMatricula(detalle.getIdDetalleMatricula());
            BigDecimal promedio = BigDecimal.ZERO;
            if (!notas.isEmpty()) {
                double suma = notas.stream().mapToDouble(n -> n.getValorNota().doubleValue()).sum();
                promedio = BigDecimal.valueOf(suma / notas.size()).setScale(2, RoundingMode.HALF_UP);
            }
            cursoDTO.setPromedioNotas(promedio);

            // --- LÓGICA: Contar Inasistencias ---
            List<Asistencia> asistencias = asistenciaRepository.findByDetalleMatricula_IdDetalleMatricula(detalle.getIdDetalleMatricula());
            long faltas = asistencias.stream()
                    .filter(a -> a.getEstado().equalsIgnoreCase("FALTA"))
                    .count();
            cursoDTO.setCantidadFaltas((int) faltas);

            // --- REGLAS DE NEGOCIO Y ALERTAS (Semáforo) ---
            if (faltas >= 3) {
                cursoDTO.setMensajeAlerta("⚠️ Peligro: Límite de inasistencias superado");
                cursoDTO.setColorSemaforo("ROJO");
            } else if (notas.isEmpty()) {
                cursoDTO.setMensajeAlerta("Sin evaluaciones registradas");
                cursoDTO.setColorSemaforo("GRIS");
            } else if (promedio.compareTo(new BigDecimal("13.00")) < 0) {
                cursoDTO.setMensajeAlerta("⚠️ En riesgo académico (Promedio bajo)");
                cursoDTO.setColorSemaforo("AMARILLO");
            } else {
                cursoDTO.setMensajeAlerta("✅ Buen rendimiento general");
                cursoDTO.setColorSemaforo("VERDE");
            }

            listaCursos.add(cursoDTO);
        }

        dashboard.setCursos(listaCursos);
        return dashboard;
    }
}