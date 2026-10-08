package com.cibertec.gestionacademicaapp.service;

import com.cibertec.gestionacademicaapp.dto.request.CicloAcademicoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CicloAcademicoResponseDTO;

import java.util.List;

public interface CicloAcademicoService {
    List<CicloAcademicoResponseDTO> listarActivos();
    CicloAcademicoResponseDTO registrar(CicloAcademicoRequestDTO requestDTO);
    CicloAcademicoResponseDTO actualizar(Integer id, CicloAcademicoRequestDTO requestDTO);
    void eliminarLogico(Integer id);
}