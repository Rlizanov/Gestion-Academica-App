package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.CarreraRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.CarreraResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Carrera;
import com.cibertec.gestionacademicaapp.repository.CarreraRepository;
import com.cibertec.gestionacademicaapp.service.CarreraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;

    @Override
    @Transactional
    public CarreraResponseDTO registrar(CarreraRequestDTO requestDTO) {
        Carrera carrera = new Carrera();
        carrera.setNombreCarrera(requestDTO.getNombreCarrera());
        carrera.setDuracionSemestres(requestDTO.getDuracionSemestres());
        carrera.setEstado(true);

        Carrera carreraGuardada = carreraRepository.save(carrera);
        return mapearAResponse(carreraGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> listarActivas() {
        return carreraRepository.findByEstadoTrue()
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private CarreraResponseDTO mapearAResponse(Carrera carrera) {
        CarreraResponseDTO dto = new CarreraResponseDTO();
        dto.setIdCarrera(carrera.getIdCarrera());
        dto.setNombreCarrera(carrera.getNombreCarrera());
        dto.setDuracionSemestres(carrera.getDuracionSemestres());
        dto.setEstado(carrera.getEstado());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO obtenerPorId(Integer id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + id));
        return mapearAResponse(carrera);
    }

    @Override
    @Transactional
    public CarreraResponseDTO actualizar(Integer id, CarreraRequestDTO requestDTO) {
        Carrera carreraExistente = carreraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + id));

        carreraExistente.setNombreCarrera(requestDTO.getNombreCarrera());
        carreraExistente.setDuracionSemestres(requestDTO.getDuracionSemestres());
        // No tocamos el estado ni el ID

        Carrera carreraActualizada = carreraRepository.save(carreraExistente);
        return mapearAResponse(carreraActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Carrera carreraExistente = carreraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + id));

        // Eliminación lógica (Soft Delete)
        carreraExistente.setEstado(false);
        carreraRepository.save(carreraExistente);
    }
}