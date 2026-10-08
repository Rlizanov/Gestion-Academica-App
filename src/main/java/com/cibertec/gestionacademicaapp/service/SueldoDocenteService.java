package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.SueldoDocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.SueldoDocenteResponseDTO;

import java.util.List;

public interface SueldoDocenteService {
    SueldoDocenteResponseDTO registrar(SueldoDocenteRequestDTO requestDTO);
    List<SueldoDocenteResponseDTO> listarPorDocente(Integer idDocente);

    SueldoDocenteResponseDTO obtenerPorId(Integer id);
    SueldoDocenteResponseDTO actualizar(Integer id, SueldoDocenteRequestDTO requestDTO);
    void eliminar(Integer id);
}