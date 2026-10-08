package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.AsistenciaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AsistenciaResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Asistencia;
import com.cibertec.gestionacademicaapp.entity.DetalleMatricula;
import com.cibertec.gestionacademicaapp.repository.AsistenciaRepository;
import com.cibertec.gestionacademicaapp.repository.DetalleMatriculaRepository;
import com.cibertec.gestionacademicaapp.service.AsistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final DetalleMatriculaRepository detalleRepository;

    @Override
    @Transactional
    public AsistenciaResponseDTO registrar(AsistenciaRequestDTO requestDTO) {
        DetalleMatricula detalle = detalleRepository.findById(requestDTO.getIdDetalleMatricula())
                .orElseThrow(() -> new RuntimeException("Detalle de matrícula no encontrado"));

        Asistencia asistencia = new Asistencia();
        asistencia.setDetalleMatricula(detalle);
        asistencia.setFecha(requestDTO.getFecha());
        asistencia.setEstado(requestDTO.getEstado().toUpperCase());
        asistencia.setObservacion(requestDTO.getObservacion());

        Asistencia asistenciaGuardada = asistenciaRepository.save(asistencia);

        return mapearAResponse(asistenciaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsistenciaResponseDTO> listarPorDetalle(Integer idDetalleMatricula) {
        return asistenciaRepository.findByDetalleMatricula_IdDetalleMatricula(idDetalleMatricula)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private AsistenciaResponseDTO mapearAResponse(Asistencia asistencia) {
        AsistenciaResponseDTO dto = new AsistenciaResponseDTO();
        dto.setIdAsistencia(asistencia.getIdAsistencia());
        dto.setIdDetalleMatricula(asistencia.getDetalleMatricula().getIdDetalleMatricula());

        // Navegación profunda hacia el Maestro para extraer nombres limpios
        String nombresAlumno = asistencia.getDetalleMatricula().getMatricula().getAlumno().getNombres();
        String apellidosAlumno = asistencia.getDetalleMatricula().getMatricula().getAlumno().getApellidos();
        dto.setNombreAlumno(nombresAlumno + " " + apellidosAlumno);

        dto.setNombreCurso(asistencia.getDetalleMatricula().getCurso().getNombreCurso());

        dto.setFecha(asistencia.getFecha());
        dto.setEstado(asistencia.getEstado());
        dto.setObservacion(asistencia.getObservacion());
        dto.setFechaRegistro(asistencia.getFechaRegistro() != null ? asistencia.getFechaRegistro() : LocalDateTime.now());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public AsistenciaResponseDTO obtenerPorId(Integer id) {
        Asistencia asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));
        return mapearAResponse(asistencia);
    }

    @Override
    @Transactional
    public AsistenciaResponseDTO actualizar(Integer id, AsistenciaRequestDTO requestDTO) {
        Asistencia asistenciaExistente = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));

        // Validamos si cambió el detalle de la matrícula
        if (!asistenciaExistente.getDetalleMatricula().getIdDetalleMatricula().equals(requestDTO.getIdDetalleMatricula())) {
            DetalleMatricula detalle = detalleRepository.findById(requestDTO.getIdDetalleMatricula())
                    .orElseThrow(() -> new RuntimeException("Detalle de matrícula no encontrado"));
            asistenciaExistente.setDetalleMatricula(detalle);
        }

        // Actualizamos los datos
        asistenciaExistente.setFecha(requestDTO.getFecha());
        asistenciaExistente.setEstado(requestDTO.getEstado().toUpperCase());
        asistenciaExistente.setObservacion(requestDTO.getObservacion());

        Asistencia asistenciaActualizada = asistenciaRepository.save(asistenciaExistente);
        return mapearAResponse(asistenciaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Asistencia asistenciaExistente = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));

        // Borrado físico de la base de datos
        asistenciaRepository.delete(asistenciaExistente);
    }
}