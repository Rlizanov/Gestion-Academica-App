package com.cibertec.gestionacademicaapp.mapper;

import com.cibertec.gestionacademicaapp.dto.request.AlumnoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Alumno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

// Le indica a Spring que gestione este Mapper para poder inyectarlo en tu Service
@Mapper(componentModel = "spring")
public interface AlumnoMapper {

    // 1. DE REQUEST A ENTIDAD (Cuando creas un alumno)
    @Mapping(target = "idAlumno", ignore = true) // La BD lo genera
    @Mapping(target = "estado", constant = "true") // Forzamos el estado activo
    @Mapping(target = "carrera.idCarrera", source = "idCarrera") // Convierte el Integer a un objeto Carrera
    Alumno toEntity(AlumnoRequestDTO requestDTO);

    // 2. DE ENTIDAD A RESPONSE (Cuando devuelves los datos a la app móvil)
    @Mapping(target = "nombreCarrera", source = "carrera.nombreCarrera") // Extrae el nombre de la relación
    AlumnoResponseDTO toDto(Alumno entidad);
}