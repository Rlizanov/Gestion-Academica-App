package com.cibertec.gestionacademicaapp.mapper;

import com.cibertec.gestionacademicaapp.dto.request.CicloAcademicoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CicloAcademicoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.CicloAcademico;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CicloAcademicoMapper {
    CicloAcademicoResponseDTO toDto(CicloAcademico ciclo);
    CicloAcademico toEntity(CicloAcademicoRequestDTO requestDTO);
    List<CicloAcademicoResponseDTO> toDtoList(List<CicloAcademico> ciclos);
}