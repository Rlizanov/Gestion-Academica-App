package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.HorarioRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.HorarioResponseDTO;

import java.util.List;

public interface HorarioService {
    HorarioResponseDTO registrar(HorarioRequestDTO requestDTO);
    List<HorarioResponseDTO> listarPorCiclo(Integer idCiclo);

    HorarioResponseDTO obtenerPorId(Integer id);
    HorarioResponseDTO actualizar(Integer id, HorarioRequestDTO requestDTO);
    void eliminar(Integer id);
}