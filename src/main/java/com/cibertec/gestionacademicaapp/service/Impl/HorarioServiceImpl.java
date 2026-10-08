package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.HorarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.HorarioResponseDTO;
import com.cibertec.gestionacademicaapp.entity.CicloAcademico;
import com.cibertec.gestionacademicaapp.entity.Curso;
import com.cibertec.gestionacademicaapp.entity.Docente;
import com.cibertec.gestionacademicaapp.entity.Horario;
import com.cibertec.gestionacademicaapp.repository.CicloAcademicoRepository;
import com.cibertec.gestionacademicaapp.repository.CursoRepository;
import com.cibertec.gestionacademicaapp.repository.DocenteRepository;
import com.cibertec.gestionacademicaapp.repository.HorarioRepository;
import com.cibertec.gestionacademicaapp.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final CicloAcademicoRepository cicloRepository;

    @Override
    @Transactional
    public HorarioResponseDTO registrar(HorarioRequestDTO requestDTO) {
        if (requestDTO.getHoraInicio().isAfter(requestDTO.getHoraFin())) {
            throw new RuntimeException("La hora de inicio no puede ser posterior a la hora de fin.");
        }

        Curso curso = cursoRepository.findById(requestDTO.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Docente docente = docenteRepository.findById(requestDTO.getIdDocente())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        CicloAcademico ciclo = cicloRepository.findById(requestDTO.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado"));

        Horario horario = new Horario();
        horario.setCurso(curso);
        horario.setDocente(docente);
        horario.setCicloAcademico(ciclo);
        horario.setDiaSemana(requestDTO.getDiaSemana().toUpperCase());
        horario.setHoraInicio(requestDTO.getHoraInicio());
        horario.setHoraFin(requestDTO.getHoraFin());
        horario.setAula(requestDTO.getAula());

        Horario horarioGuardado = horarioRepository.save(horario);

        return mapearAResponse(horarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HorarioResponseDTO> listarPorCiclo(Integer idCiclo) {
        return horarioRepository.findByCicloAcademico_IdCiclo(idCiclo)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private HorarioResponseDTO mapearAResponse(Horario horario) {
        HorarioResponseDTO dto = new HorarioResponseDTO();
        dto.setIdHorario(horario.getIdHorario());
        dto.setNombreCurso(horario.getCurso().getNombreCurso());
        dto.setNombreDocente(horario.getDocente().getNombres() + " " + horario.getDocente().getApellidos());
        dto.setNombreCiclo(horario.getCicloAcademico().getNombreCiclo());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setAula(horario.getAula());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioResponseDTO obtenerPorId(Integer id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
        return mapearAResponse(horario);
    }

    @Override
    @Transactional
    public HorarioResponseDTO actualizar(Integer id, HorarioRequestDTO requestDTO) {
        // 1. Validamos lógica de horas igual que en el registro
        if (requestDTO.getHoraInicio().isAfter(requestDTO.getHoraFin())) {
            throw new RuntimeException("La hora de inicio no puede ser posterior a la hora de fin.");
        }

        // 2. Buscamos el horario que se quiere modificar
        Horario horarioExistente = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));

        // 3. Buscamos las entidades foráneas por si hubo algún cambio de profesor, curso o ciclo
        Curso curso = cursoRepository.findById(requestDTO.getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Docente docente = docenteRepository.findById(requestDTO.getIdDocente())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        CicloAcademico ciclo = cicloRepository.findById(requestDTO.getIdCiclo())
                .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado"));

        // 4. Actualizamos los datos
        horarioExistente.setCurso(curso);
        horarioExistente.setDocente(docente);
        horarioExistente.setCicloAcademico(ciclo);
        horarioExistente.setDiaSemana(requestDTO.getDiaSemana().toUpperCase());
        horarioExistente.setHoraInicio(requestDTO.getHoraInicio());
        horarioExistente.setHoraFin(requestDTO.getHoraFin());
        horarioExistente.setAula(requestDTO.getAula());

        Horario horarioActualizado = horarioRepository.save(horarioExistente);
        return mapearAResponse(horarioActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Horario horarioExistente = horarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));

        // Borrado físico real ya que no existe campo 'Estado' en esta tabla
        horarioRepository.delete(horarioExistente);
    }
}