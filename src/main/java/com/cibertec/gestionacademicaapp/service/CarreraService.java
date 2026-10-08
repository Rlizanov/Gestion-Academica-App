package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.CarreraRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CarreraResponseDTO;

import java.util.List;

public interface CarreraService {
    CarreraResponseDTO registrar(CarreraRequestDTO requestDTO);
    List<CarreraResponseDTO> listarActivas();
    CarreraResponseDTO obtenerPorId(Integer id);
    CarreraResponseDTO actualizar(Integer id, CarreraRequestDTO requestDTO);
    void eliminar(Integer id);
}