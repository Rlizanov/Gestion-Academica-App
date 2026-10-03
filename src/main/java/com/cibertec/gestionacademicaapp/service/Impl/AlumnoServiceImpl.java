package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.AlumnoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Alumno;
import com.cibertec.gestionacademicaapp.mapper.AlumnoMapper;
import com.cibertec.gestionacademicaapp.repository.AlumnoRepository;
import com.cibertec.gestionacademicaapp.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;

    @Override
    public List<AlumnoResponseDTO> listarAlumnosActivos() {
        // Usamos el método que creaste en el repositorio para traer solo los activos
        List<Alumno> alumnos = alumnoRepository.findByEstado(true);

        // Convertimos la lista de Entidades a lista de DTOs usando Streams de Java y el Mapper
        return alumnos.stream()
                .map(alumnoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AlumnoResponseDTO obtenerPorId(Integer id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
        return alumnoMapper.toDto(alumno);
    }

    @Override
    public AlumnoResponseDTO registrarAlumno(AlumnoRequestDTO requestDTO) {
        // 1. Validar reglas de negocio
        if (alumnoRepository.findByDni(requestDTO.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado.");
        }
        if (alumnoRepository.findByCorreo(requestDTO.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // 2. Convertir el DTO a Entidad
        Alumno nuevoAlumno = alumnoMapper.toEntity(requestDTO);

        // 3. Guardar en SQL Server
        Alumno alumnoGuardado = alumnoRepository.save(nuevoAlumno);

        // 4. Devolver la respuesta mapeada
        return alumnoMapper.toDto(alumnoGuardado);
    }

    @Override
    public AlumnoResponseDTO actualizarAlumno(Integer id, AlumnoRequestDTO requestDTO) {
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));

        // Actualizamos los datos (no tocamos el ID ni el estado)
        alumnoExistente.setNombres(requestDTO.getNombres());
        alumnoExistente.setApellidos(requestDTO.getApellidos());
        alumnoExistente.setDni(requestDTO.getDni());
        alumnoExistente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        alumnoExistente.setCorreo(requestDTO.getCorreo());
        alumnoExistente.setTelefono(requestDTO.getTelefono());

        // Aquí deberías actualizar la carrera también, pero requeriría buscar la entidad Carrera primero.
        // Por simplicidad en este paso, actualizamos los datos básicos.

        Alumno alumnoActualizado = alumnoRepository.save(alumnoExistente);
        return alumnoMapper.toDto(alumnoActualizado);
    }

    @Override
    public void eliminarAlumno(Integer id) {
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));

        // Hacemos una eliminación lógica (Soft Delete) en lugar de borrar el registro físico
        alumnoExistente.setEstado(false);
        alumnoRepository.save(alumnoExistente);
    }
}