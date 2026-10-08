package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.CicloAcademicoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CicloAcademicoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.CicloAcademico;
import com.cibertec.gestionacademicaapp.mapper.CicloAcademicoMapper;
import com.cibertec.gestionacademicaapp.repository.CicloAcademicoRepository;
import com.cibertec.gestionacademicaapp.service.CicloAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CicloAcademicoServiceImpl implements CicloAcademicoService {

    private final CicloAcademicoRepository cicloRepository;
    private final CicloAcademicoMapper cicloMapper;

    @Override
    public List<CicloAcademicoResponseDTO> listarActivos() {
        return cicloMapper.toDtoList(cicloRepository.findByEstado(true));
    }

    @Override
    public CicloAcademicoResponseDTO registrar(CicloAcademicoRequestDTO requestDTO) {
        validarFechas(requestDTO);
        CicloAcademico ciclo = cicloMapper.toEntity(requestDTO);
        ciclo.setEstado(true);
        return cicloMapper.toDto(cicloRepository.save(ciclo));
    }

    @Override
    public CicloAcademicoResponseDTO actualizar(Integer id, CicloAcademicoRequestDTO requestDTO) {
        validarFechas(requestDTO);
        CicloAcademico cicloExistente = cicloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado"));

        cicloExistente.setNombreCiclo(requestDTO.getNombreCiclo());
        cicloExistente.setFechaInicio(requestDTO.getFechaInicio());
        cicloExistente.setFechaFin(requestDTO.getFechaFin());

        return cicloMapper.toDto(cicloRepository.save(cicloExistente));
    }

    @Override
    public void eliminarLogico(Integer id) {
        CicloAcademico cicloExistente = cicloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciclo académico no encontrado"));
        cicloExistente.setEstado(false);
        cicloRepository.save(cicloExistente);
    }

    // Validación extra de negocio
    private void validarFechas(CicloAcademicoRequestDTO requestDTO) {
        if (requestDTO.getFechaFin().isBefore(requestDTO.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }
}