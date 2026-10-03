package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.AlumnoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AlumnoResponseDTO;

import java.util.List;

public interface AlumnoService {
    List<AlumnoResponseDTO> listarAlumnosActivos();
    AlumnoResponseDTO obtenerPorId(Integer id);
    AlumnoResponseDTO registrarAlumno(AlumnoRequestDTO requestDTO);
    AlumnoResponseDTO actualizarAlumno(Integer id, AlumnoRequestDTO requestDTO);
    void eliminarAlumno(Integer id); // Eliminación lógica (cambiar estado a false)
}