package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.UsuarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.UsuarioResponseDTO;

public interface UsuarioService {
    UsuarioResponseDTO registrar(UsuarioRequestDTO requestDTO); // Nuevo método
}