package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.MatriculaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.DetalleMatriculaResponseDTO;
import com.cibertec.gestionacademicaapp.dto.response.MatriculaResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Alumno;
import com.cibertec.gestionacademicaapp.entity.CicloAcademico;
import com.cibertec.gestionacademicaapp.entity.Curso;
import com.cibertec.gestionacademicaapp.entity.DetalleMatricula;
import com.cibertec.gestionacademicaapp.entity.Matricula;
import com.cibertec.gestionacademicaapp.repository.AlumnoRepository;
import com.cibertec.gestionacademicaapp.repository.CicloAcademicoRepository;
import com.cibertec.gestionacademicaapp.repository.CursoRepository;
import com.cibertec.gestionacademicaapp.repository.DetalleMatriculaRepository;
import com.cibertec.gestionacademicaapp.repository.MatriculaRepository;
import com.cibertec.gestionacademicaapp.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final DetalleMatriculaRepository detalleRepository;
    private final AlumnoRepository alumnoRepository;
    private final CicloAcademicoRepository cicloRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional
    public MatriculaResponseDTO registrar(MatriculaRequestDTO requestDTO) {

        // 1. Validar existencia de Alumno y Ciclo Académico
        Alumno alumno = alumnoRepository.findById(requestDTO.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + requestDTO.getIdAlumno()));

        CicloAcademico ciclo = cicloRepository.findById(requestDTO.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado con ID: " + requestDTO.getIdCiclo()));

        // 2. Crear y guardar la cabecera (Matricula)
        Matricula matricula = new Matricula();
        matricula.setAlumno(alumno);
        matricula.setCicloAcademico(ciclo);
        matricula.setEstado("ACTIVA");

        Matricula matriculaGuardada = matriculaRepository.save(matricula);

        // 3. Crear y guardar cada DetalleMatricula
        List<DetalleMatriculaResponseDTO> detallesResponse = new ArrayList<>();

        for (Integer idCurso : requestDTO.getCursosIds()) {
            Curso curso = cursoRepository.findById(idCurso)
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + idCurso));

            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setMatricula(matriculaGuardada);
            detalle.setCurso(curso);

            DetalleMatricula detalleGuardado = detalleRepository.save(detalle);

            DetalleMatriculaResponseDTO detDTO = new DetalleMatriculaResponseDTO();
            detDTO.setIdDetalleMatricula(detalleGuardado.getIdDetalleMatricula());
            detDTO.setIdCurso(curso.getIdCurso());
            detallesResponse.add(detDTO);
        }

        // 4. Construir la respuesta final
        MatriculaResponseDTO response = new MatriculaResponseDTO();
        response.setIdMatricula(matriculaGuardada.getIdMatricula());
        response.setIdAlumno(alumno.getIdAlumno());
        response.setIdCiclo(ciclo.getIdCiclo());
        // Como 'FechaRegistro' tiene insertable=false, en la entidad en memoria aún está null hasta recargarla;
        // asignamos LocalDateTime.now() para la respuesta del endpoint.
        response.setFechaRegistro(matriculaGuardada.getFechaRegistro() != null
                ? matriculaGuardada.getFechaRegistro()
                : LocalDateTime.now());
        response.setEstado(matriculaGuardada.getEstado());
        response.setDetalles(detallesResponse);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarActivas() {
        // 1. Buscamos todas las matrículas con estado "ACTIVA"
        List<Matricula> matriculas = matriculaRepository.findByEstado("ACTIVA");
        List<MatriculaResponseDTO> responseList = new ArrayList<>();

        for (Matricula matricula : matriculas) {
            MatriculaResponseDTO dto = new MatriculaResponseDTO();
            dto.setIdMatricula(matricula.getIdMatricula());
            // Como usamos @ManyToOne, extraemos los IDs navegando por los objetos
            dto.setIdAlumno(matricula.getAlumno().getIdAlumno());
            dto.setIdCiclo(matricula.getCicloAcademico().getIdCiclo());
            dto.setFechaRegistro(matricula.getFechaRegistro());
            dto.setEstado(matricula.getEstado());

            // 2. Buscamos los detalles (cursos) para esta matrícula específica
            List<DetalleMatricula> detalles = detalleRepository.findByMatricula_IdMatricula(matricula.getIdMatricula());
            List<DetalleMatriculaResponseDTO> detallesDTO = new ArrayList<>();

            for (DetalleMatricula detalle : detalles) {
                DetalleMatriculaResponseDTO detDTO = new DetalleMatriculaResponseDTO();
                detDTO.setIdDetalleMatricula(detalle.getIdDetalleMatricula());
                detDTO.setIdCurso(detalle.getCurso().getIdCurso());
                detallesDTO.add(detDTO);
            }

            dto.setDetalles(detallesDTO);
            responseList.add(dto);
        }

        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
    public MatriculaResponseDTO obtenerPorId(Integer id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada con ID: " + id));
        return mapearAResponse(matricula);
    }

    @Override
    @Transactional
    public MatriculaResponseDTO actualizar(Integer id, MatriculaRequestDTO requestDTO) {
        Matricula matriculaExistente = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada con ID: " + id));

        // Validamos si se corrigió el alumno
        if (!matriculaExistente.getAlumno().getIdAlumno().equals(requestDTO.getIdAlumno())) {
            Alumno alumno = alumnoRepository.findById(requestDTO.getIdAlumno())
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
            matriculaExistente.setAlumno(alumno);
        }

        // Validamos si se corrigió el ciclo académico
        if (!matriculaExistente.getCicloAcademico().getIdCiclo().equals(requestDTO.getIdCiclo())) {
            CicloAcademico ciclo = cicloRepository.findById(requestDTO.getIdCiclo())
                    .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado"));
            matriculaExistente.setCicloAcademico(ciclo);
        }

        // Guardamos los cambios (no alteramos la fecha de registro ni el estado en un PUT estándar)
        Matricula matriculaActualizada = matriculaRepository.save(matriculaExistente);
        return mapearAResponse(matriculaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Matricula matriculaExistente = matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada con ID: " + id));

        // Eliminación lógica (Soft Delete): Cambiamos el estado a ANULADA
        matriculaExistente.setEstado("ANULADA");
        matriculaRepository.save(matriculaExistente);
    }

    // Método auxiliar para mapear una Entidad Matricula a MatriculaResponseDTO
    private MatriculaResponseDTO mapearAResponse(Matricula matricula) {
        MatriculaResponseDTO dto = new MatriculaResponseDTO();
        dto.setIdMatricula(matricula.getIdMatricula());
        dto.setIdAlumno(matricula.getAlumno().getIdAlumno());
        dto.setIdCiclo(matricula.getCicloAcademico().getIdCiclo());

        dto.setFechaRegistro(matricula.getFechaRegistro() != null
                ? matricula.getFechaRegistro()
                : LocalDateTime.now());

        dto.setEstado(matricula.getEstado());

        // Extraer los detalles (cursos) de la matrícula
        List<DetalleMatricula> detalles = detalleRepository.findByMatricula_IdMatricula(matricula.getIdMatricula());
        List<DetalleMatriculaResponseDTO> detallesDTO = new ArrayList<>();

        for (DetalleMatricula detalle : detalles) {
            DetalleMatriculaResponseDTO detDTO = new DetalleMatriculaResponseDTO();
            detDTO.setIdDetalleMatricula(detalle.getIdDetalleMatricula());
            detDTO.setIdCurso(detalle.getCurso().getIdCurso());
            detallesDTO.add(detDTO);
        }

        dto.setDetalles(detallesDTO);
        return dto;
    }
}