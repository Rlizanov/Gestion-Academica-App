package com.cibertec.gestionacademicaapp.service.impl;

import com.cibertec.gestionacademicaapp.dto.request.CursoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CursoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Curso;
import com.cibertec.gestionacademicaapp.mapper.CursoMapper;
import com.cibertec.gestionacademicaapp.repository.CursoRepository;
import com.cibertec.gestionacademicaapp.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    @Override
    public List<CursoResponseDTO> listarActivos() {
        return cursoMapper.toDtoList(cursoRepository.findByEstado(true));
    }

    @Override
    public CursoResponseDTO registrar(CursoRequestDTO requestDTO) {
        Curso curso = cursoMapper.toEntity(requestDTO);
        curso.setEstado(true); // Siempre activo al registrar
        return cursoMapper.toDto(cursoRepository.save(curso));
    }

    @Override
    public CursoResponseDTO actualizar(Integer id, CursoRequestDTO requestDTO) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        cursoExistente.setNombreCurso(requestDTO.getNombreCurso());
        cursoExistente.setCreditos(requestDTO.getCreditos());
        cursoExistente.setHorasSemanales(requestDTO.getHorasSemanales());

        return cursoMapper.toDto(cursoRepository.save(cursoExistente));
    }

    @Override
    public void eliminarLogico(Integer id) {
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        cursoExistente.setEstado(false); // Eliminación lógica
        cursoRepository.save(cursoExistente);
    }
}