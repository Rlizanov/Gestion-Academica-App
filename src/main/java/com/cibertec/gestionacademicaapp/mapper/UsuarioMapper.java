package com.cibertec.gestionacademicaapp.mapper;

import com.cibertec.gestionacademicaapp.dto.request.UsuarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.UsuarioResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    // Si tu entidad Usuario tiene una relación con Rol (ej. usuario.getRol().getNombreRol()),
    // MapStruct lo mapeará automáticamente si los nombres coinciden, o puedes forzarlo así:
    // @Mapping(source = "rol.nombreRol", target = "rol")
    UsuarioResponseDTO toDto(Usuario usuario);

    Usuario toEntity(UsuarioRequestDTO requestDTO);
}