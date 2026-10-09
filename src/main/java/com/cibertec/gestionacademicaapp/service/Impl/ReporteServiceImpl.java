package com.cibertec.gestionacademicaapp.service.Impl;


import com.cibertec.gestionacademicaapp.dto.reporte.ClaseAgendaDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.CursoRendimientoDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.*;
import com.cibertec.gestionacademicaapp.repository.*;
import com.cibertec.gestionacademicaapp.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final MatriculaRepository matriculaRepository;
    private final DetalleMatriculaRepository detalleRepository;
    private final NotaRepository notaRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final HorarioRepository horarioRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<ClaseAgendaDTO> obtenerAgendaHoyAlumno(Integer idAlumno) {

        // 1. Obtener la matrícula activa
        Matricula matricula = matriculaRepository.findByAlumno_IdAlumnoAndEstado(idAlumno, "ACTIVA")
                .orElseThrow(() -> new RuntimeException("El alumno no tiene una matrícula activa."));

        // 2. Extraer los IDs de los cursos en los que está matriculado
        List<DetalleMatricula> detalles = detalleRepository.findByMatricula_IdMatricula(matricula.getIdMatricula());
        List<Integer> idsCursosMatriculados = detalles.stream()
                .map(d -> d.getCurso().getIdCurso())
                .toList();

        // 3. Obtener el día actual en español (LUNES, MARTES, etc.)
        String diaHoy = obtenerDiaSemanaActual();

        // 4. Buscar el horario de hoy ordenado por hora de inicio
        List<Horario> horariosDeHoy = horarioRepository
                .findByCicloAcademico_IdCicloAndDiaSemanaAndCurso_IdCursoInOrderByHoraInicioAsc(
                        matricula.getCicloAcademico().getIdCiclo(),
                        diaHoy,
                        idsCursosMatriculados);

        // 5. Mapear al DTO y calcular el estado en vivo de la clase
        List<ClaseAgendaDTO> agenda = new ArrayList<>();
        LocalTime horaActual = LocalTime.now();

        for (Horario h : horariosDeHoy) {
            ClaseAgendaDTO dto = new ClaseAgendaDTO();
            dto.setNombreCurso(h.getCurso().getNombreCurso());
            dto.setNombreDocente(h.getDocente().getNombres() + " " + h.getDocente().getApellidos());
            dto.setAula(h.getAula());
            dto.setHoraInicio(h.getHoraInicio());
            dto.setHoraFin(h.getHoraFin());

            // --- REGLA DE NEGOCIO: Estado de la clase en tiempo real ---
            if (horaActual.isAfter(h.getHoraFin())) {
                dto.setEstadoClase("FINALIZADA");
            } else if (horaActual.isBefore(h.getHoraInicio())) {
                dto.setEstadoClase("POR INICIAR");
            } else {
                dto.setEstadoClase("EN CURSO");
            }

            agenda.add(dto);
        }

        return agenda;
    }

    // Método auxiliar para traducir el día de la máquina a tu base de datos
    private String obtenerDiaSemanaActual() {
        java.time.DayOfWeek dayOfWeek = java.time.LocalDate.now().getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> "LUNES";
            case TUESDAY -> "MARTES";
            case WEDNESDAY -> "MIERCOLES";
            case THURSDAY -> "JUEVES";
            case FRIDAY -> "VIERNES";
            case SATURDAY -> "SABADO";
            case SUNDAY -> "DOMINGO";
        };
    }
}