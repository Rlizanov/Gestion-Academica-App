package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.UsuarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.UsuarioResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Usuario;
import com.cibertec.gestionacademicaapp.mapper.UsuarioMapper;
import com.cibertec.gestionacademicaapp.repository.UsuarioRepository;
import com.cibertec.gestionacademicaapp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO registrar(UsuarioRequestDTO requestDTO) {
        // Convertimos el request a entidad
        Usuario nuevoUsuario = usuarioMapper.toEntity(requestDTO);

        // Encriptamos la contraseña plana y la guardamos en la columna PasswordHash
        String hash = passwordEncoder.encode(requestDTO.getPassword());
        nuevoUsuario.setPasswordHash(hash);

        // Asignamos el estado activo por defecto
        nuevoUsuario.setEstado(true);

        // Guardamos en SQL Server
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return usuarioMapper.toDto(usuarioGuardado);
    }
}