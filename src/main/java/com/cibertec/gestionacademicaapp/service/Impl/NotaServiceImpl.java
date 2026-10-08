package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.NotaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.NotaResponseDTO;
import com.cibertec.gestionacademicaapp.entity.DetalleMatricula;
import com.cibertec.gestionacademicaapp.entity.Nota;
import com.cibertec.gestionacademicaapp.repository.DetalleMatriculaRepository;
import com.cibertec.gestionacademicaapp.repository.NotaRepository;
import com.cibertec.gestionacademicaapp.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotaServiceImpl implements NotaService {

    private final NotaRepository notaRepository;
    private final DetalleMatriculaRepository detalleRepository;

    @Override
    @Transactional
    public NotaResponseDTO registrar(NotaRequestDTO requestDTO) {

        DetalleMatricula detalle = detalleRepository.findById(requestDTO.getIdDetalleMatricula())
                .orElseThrow(() -> new RuntimeException("Detalle de matrícula no encontrado"));

        Nota nota = new Nota();
        nota.setDetalleMatricula(detalle);
        nota.setValorNota(requestDTO.getNota());
        nota.setTipoEvaluacion(requestDTO.getTipoEvaluacion());
        nota.setFechaEvaluacion(requestDTO.getFechaEvaluacion());

        Nota notaGuardada = notaRepository.save(nota);

        return mapearAResponse(notaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaResponseDTO> listarPorDetalle(Integer idDetalleMatricula) {
        return notaRepository.findByDetalleMatricula_IdDetalleMatricula(idDetalleMatricula)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private NotaResponseDTO mapearAResponse(Nota nota) {
        NotaResponseDTO dto = new NotaResponseDTO();
        dto.setIdNota(nota.getIdNota());
        dto.setIdDetalleMatricula(nota.getDetalleMatricula().getIdDetalleMatricula());
        dto.setNombreCurso(nota.getDetalleMatricula().getCurso().getNombreCurso());
        dto.setNota(nota.getValorNota());
        dto.setTipoEvaluacion(nota.getTipoEvaluacion());
        dto.setFechaEvaluacion(nota.getFechaEvaluacion());
        dto.setFechaRegistro(nota.getFechaRegistro() != null ? nota.getFechaRegistro() : LocalDateTime.now());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public NotaResponseDTO obtenerPorId(Integer id) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + id));
        return mapearAResponse(nota);
    }

    @Override
    @Transactional
    public NotaResponseDTO actualizar(Integer id, NotaRequestDTO requestDTO) {
        Nota notaExistente = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + id));

        // Por si el usuario corrige y cambia el curso (DetalleMatricula) al que pertenece la nota
        if (!notaExistente.getDetalleMatricula().getIdDetalleMatricula().equals(requestDTO.getIdDetalleMatricula())) {
            DetalleMatricula detalle = detalleRepository.findById(requestDTO.getIdDetalleMatricula())
                    .orElseThrow(() -> new RuntimeException("Detalle de matrícula no encontrado"));
            notaExistente.setDetalleMatricula(detalle);
        }

        // Actualizamos los valores de la evaluación
        notaExistente.setValorNota(requestDTO.getNota());
        notaExistente.setTipoEvaluacion(requestDTO.getTipoEvaluacion());
        notaExistente.setFechaEvaluacion(requestDTO.getFechaEvaluacion());

        Nota notaActualizada = notaRepository.save(notaExistente);
        return mapearAResponse(notaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Nota notaExistente = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con ID: " + id));

        // Como la tabla NO tiene campo 'Estado', hacemos un borrado físico
        notaRepository.delete(notaExistente);
    }
}