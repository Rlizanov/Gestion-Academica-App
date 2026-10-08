package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.DocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.DocenteResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Docente;
import com.cibertec.gestionacademicaapp.mapper.DocenteMapper;
import com.cibertec.gestionacademicaapp.repository.DocenteRepository;
import com.cibertec.gestionacademicaapp.service.DocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final DocenteMapper docenteMapper;

    @Override
    public List<DocenteResponseDTO> listarActivos() {
        List<Docente> docentes = docenteRepository.findByEstado(true);
        return docenteMapper.toDtoList(docentes);
    }

    @Override
    public DocenteResponseDTO registrar(DocenteRequestDTO requestDTO) {
        Docente docente = docenteMapper.toEntity(requestDTO);
        docente.setEstado(true); // Siempre se crea como activo
        Docente guardado = docenteRepository.save(docente);
        return docenteMapper.toDto(guardado);
    }

    @Override
    public DocenteResponseDTO actualizar(Integer id, DocenteRequestDTO requestDTO) {
        Docente docenteExistente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        docenteExistente.setNombres(requestDTO.getNombres());
        docenteExistente.setApellidos(requestDTO.getApellidos());
        docenteExistente.setDni(requestDTO.getDni());
        docenteExistente.setCorreo(requestDTO.getCorreo());
        docenteExistente.setTelefono(requestDTO.getTelefono());

        // Nuevos campos mapeados de tu BBDD
        docenteExistente.setEspecialidad(requestDTO.getEspecialidad());
        docenteExistente.setFechaIngreso(requestDTO.getFechaIngreso());

        Docente actualizado = docenteRepository.save(docenteExistente);
        return docenteMapper.toDto(actualizado);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Docente docenteExistente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        docenteExistente.setEstado(false); // Soft delete
        docenteRepository.save(docenteExistente);
    }
}