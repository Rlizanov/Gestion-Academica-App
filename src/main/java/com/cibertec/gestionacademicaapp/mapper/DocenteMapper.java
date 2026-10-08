package com.cibertec.gestionacademicaapp.mapper;

import com.cibertec.gestionacademicaapp.dto.request.DocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.DocenteResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Docente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocenteMapper {
    DocenteResponseDTO toDto(Docente docente);
    Docente toEntity(DocenteRequestDTO requestDTO);
    List<DocenteResponseDTO> toDtoList(List<Docente> docentes);
}