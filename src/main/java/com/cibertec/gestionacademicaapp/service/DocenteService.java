package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.DocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.DocenteResponseDTO;

import java.util.List;

public interface DocenteService {
    List<DocenteResponseDTO> listarActivos();
    DocenteResponseDTO registrar(DocenteRequestDTO requestDTO);
    DocenteResponseDTO actualizar(Integer id, DocenteRequestDTO requestDTO);
    void eliminarLogico(Integer id);
}