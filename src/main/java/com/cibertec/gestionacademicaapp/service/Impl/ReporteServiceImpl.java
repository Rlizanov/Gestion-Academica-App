package com.cibertec.gestionacademicaapp.service.Impl;


import com.cibertec.gestionacademicaapp.dto.reporte.ClaseAgendaDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.CursoRendimientoDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.DashboardAlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.dto.reporte.SimuladorNotaDTO;
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

    @Override
    @Transactional(readOnly = true)
    public SimuladorNotaDTO simularAprobacion(Integer idAlumno, Integer idCurso) {

        // 1. Obtener la matrícula activa
        Matricula matricula = matriculaRepository.findByAlumno_IdAlumnoAndEstado(idAlumno, "ACTIVA")
                .orElseThrow(() -> new RuntimeException("El alumno no tiene una matrícula activa."));

        // 2. Encontrar el detalle de ese curso específico
        List<DetalleMatricula> detalles = detalleRepository.findByMatricula_IdMatricula(matricula.getIdMatricula());
        DetalleMatricula detalleCurso = detalles.stream()
                .filter(d -> d.getCurso().getIdCurso().equals(idCurso))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El alumno no está matriculado en este curso."));

        // 3. Obtener las notas actuales
        List<Nota> notas = notaRepository.findByDetalleMatricula_IdDetalleMatricula(detalleCurso.getIdDetalleMatricula());

        // 4. REGLAS DE NEGOCIO CIBERTEC: 3 Notas (T1, T2, EF) y nota mínima 13
        int TOTAL_NOTAS_CICLO = 3;
        double NOTA_APROBATORIA = 13.0;

        int notasRegistradas = notas.size();
        int notasFaltantes = TOTAL_NOTAS_CICLO - notasRegistradas;

        double sumaActual = notas.stream().mapToDouble(n -> n.getValorNota().doubleValue()).sum();
        double promedioActual = notasRegistradas == 0 ? 0.0 : sumaActual / notasRegistradas;

        // 5. Armar la respuesta
        SimuladorNotaDTO dto = new SimuladorNotaDTO();
        dto.setNombreCurso(detalleCurso.getCurso().getNombreCurso());
        dto.setNotasRegistradas(notasRegistradas);
        dto.setNotasFaltantes(notasFaltantes);
        dto.setPromedioActual(BigDecimal.valueOf(promedioActual).setScale(2, java.math.RoundingMode.HALF_UP));

        // 6. Lógica del Simulador
        if (notasFaltantes <= 0) { // Ya dio el EF
            dto.setNotasFaltantes(0);
            dto.setNotaMinimaRequerida(BigDecimal.ZERO);
            if (sumaActual / TOTAL_NOTAS_CICLO >= NOTA_APROBATORIA) {
                dto.setMensajeAlerta("¡Felicidades! Curso aprobado.");
                dto.setColorSemaforo("VERDE");
            } else {
                dto.setMensajeAlerta("Curso desaprobado. Nos vemos en el sustitutorio.");
                dto.setColorSemaforo("ROJO");
            }
        } else {
            // Ecuación para Cibertec: (SumaActual + (NotaRequerida * NotasFaltantes)) / 3 = 13
            double puntosFaltantes = (NOTA_APROBATORIA * TOTAL_NOTAS_CICLO) - sumaActual;
            double notaRequerida = puntosFaltantes / notasFaltantes;

            if (notaRequerida > 20.0) {
                dto.setNotaMinimaRequerida(BigDecimal.valueOf(20.00).setScale(2, java.math.RoundingMode.HALF_UP));
                dto.setMensajeAlerta("Matemáticamente imposible. Necesitas más de 20 en lo que falta.");
                dto.setColorSemaforo("ROJO");
            } else if (notaRequerida <= 0) {
                dto.setNotaMinimaRequerida(BigDecimal.ZERO);
                dto.setMensajeAlerta("¡Ya aseguraste el curso con tus notas actuales!");
                dto.setColorSemaforo("VERDE");
            } else {
                dto.setNotaMinimaRequerida(BigDecimal.valueOf(notaRequerida).setScale(2, java.math.RoundingMode.HALF_UP));
                dto.setMensajeAlerta("Necesitas sacar esta nota en tus siguientes evaluaciones para aprobar.");
                dto.setColorSemaforo("AMARILLO");
            }
        }

        return dto;
    }
}