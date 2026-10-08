package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.MatriculaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.MatriculaResponseDTO;

import java.util.List;

public interface MatriculaService {
    MatriculaResponseDTO registrar(MatriculaRequestDTO requestDTO);
    List<MatriculaResponseDTO> listarActivas();

    MatriculaResponseDTO obtenerPorId(Integer id);
    MatriculaResponseDTO actualizar(Integer id, MatriculaRequestDTO requestDTO);
    void eliminar(Integer id);
}