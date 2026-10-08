package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.AsistenciaRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AsistenciaResponseDTO;

import java.util.List;

public interface AsistenciaService {
    AsistenciaResponseDTO registrar(AsistenciaRequestDTO requestDTO);
    List<AsistenciaResponseDTO> listarPorDetalle(Integer idDetalleMatricula);

    AsistenciaResponseDTO obtenerPorId(Integer id);
    AsistenciaResponseDTO actualizar(Integer id, AsistenciaRequestDTO requestDTO);
    void eliminar(Integer id);
}