package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.AlumnoRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.AlumnoResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Alumno;
import com.cibertec.gestionacademicaapp.entity.Carrera;
import com.cibertec.gestionacademicaapp.mapper.AlumnoMapper;
import com.cibertec.gestionacademicaapp.repository.AlumnoRepository;
import com.cibertec.gestionacademicaapp.repository.CarreraRepository;
import com.cibertec.gestionacademicaapp.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;
    private final CarreraRepository carreraRepository;

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
    @Transactional
    public AlumnoResponseDTO registrarAlumno(AlumnoRequestDTO requestDTO) {
        // 1. Validar reglas de negocio
        if (alumnoRepository.findByDni(requestDTO.getDni()).isPresent()) {
            throw new RuntimeException("El DNI ya está registrado.");
        }
        if (alumnoRepository.findByCorreo(requestDTO.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // 2. Validar que la carrera exista en BD
        Carrera carrera = carreraRepository.findById(requestDTO.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + requestDTO.getIdCarrera()));

        // 3. Convertir el DTO a Entidad y asegurar la carrera real
        Alumno nuevoAlumno = alumnoMapper.toEntity(requestDTO);
        nuevoAlumno.setCarrera(carrera); // Reforzamos asignando la entidad administrada por JPA

        // 4. Guardar en SQL Server
        Alumno alumnoGuardado = alumnoRepository.save(nuevoAlumno);

        // 5. Devolver la respuesta mapeada
        return alumnoMapper.toDto(alumnoGuardado);
    }

    @Override
    @Transactional
    public AlumnoResponseDTO actualizarAlumno(Integer id, AlumnoRequestDTO requestDTO) {
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));

        // Validar que la nueva carrera exista
        Carrera carrera = carreraRepository.findById(requestDTO.getIdCarrera())
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada con ID: " + requestDTO.getIdCarrera()));

        // Actualizamos los datos
        alumnoExistente.setNombres(requestDTO.getNombres());
        alumnoExistente.setApellidos(requestDTO.getApellidos());
        alumnoExistente.setDni(requestDTO.getDni());
        alumnoExistente.setFechaNacimiento(requestDTO.getFechaNacimiento());
        alumnoExistente.setCorreo(requestDTO.getCorreo());
        alumnoExistente.setTelefono(requestDTO.getTelefono());

        // ¡Ahora sí actualizamos la carrera!
        alumnoExistente.setCarrera(carrera);

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