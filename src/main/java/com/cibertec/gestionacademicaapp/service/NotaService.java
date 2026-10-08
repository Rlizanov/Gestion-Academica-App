package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.MatriculaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.request.NotaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.MatriculaResponseDTO;
import com.cibertec.gestionacademicaapp.dto.response.NotaResponseDTO;

import java.util.List;

public interface NotaService {
    NotaResponseDTO registrar (NotaRequestDTO matriculaRequestDTO);
    List<NotaResponseDTO> listarPorDetalle (Integer id);
    NotaResponseDTO obtenerPorId(Integer id);
    NotaResponseDTO actualizar(Integer id, NotaRequestDTO requestDTO);
    void eliminar(Integer id);
}
