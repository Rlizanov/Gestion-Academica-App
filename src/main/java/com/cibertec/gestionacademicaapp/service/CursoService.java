package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.CursoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CursoResponseDTO;

import java.util.List;

public interface CursoService {
    List<CursoResponseDTO> listarActivos();
    CursoResponseDTO registrar(CursoRequestDTO requestDTO);
    CursoResponseDTO actualizar(Integer id, CursoRequestDTO requestDTO);
    void eliminarLogico(Integer id);
}