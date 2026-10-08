package com.cibertec.gestionacademicaapp.mapper;

import com.cibertec.gestionacademicaapp.dto.request.CursoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CursoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Curso;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CursoMapper {
    CursoResponseDTO toDto(Curso curso);
    Curso toEntity(CursoRequestDTO requestDTO);
    List<CursoResponseDTO> toDtoList(List<Curso> cursos);
}