package com.cibertec.gestionacademicaapp.service.Impl;

import com.cibertec.gestionacademicaapp.dto.request.SueldoDocenteRequestDTO;
import com.cibertec.gestionacademicaapp.dto.response.SueldoDocenteResponseDTO;
import com.cibertec.gestionacademicaapp.entity.Docente;
import com.cibertec.gestionacademicaapp.entity.SueldoDocente;
import com.cibertec.gestionacademicaapp.repository.DocenteRepository;
import com.cibertec.gestionacademicaapp.repository.SueldoDocenteRepository;
import com.cibertec.gestionacademicaapp.service.SueldoDocenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SueldoDocenteServiceImpl implements SueldoDocenteService {

    private final SueldoDocenteRepository sueldoRepository;
    private final DocenteRepository docenteRepository;

    @Override
    @Transactional
    public SueldoDocenteResponseDTO registrar(SueldoDocenteRequestDTO requestDTO) {

        Docente docente = docenteRepository.findById(requestDTO.getIdDocente())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado con ID: " + requestDTO.getIdDocente()));

        SueldoDocente sueldo = new SueldoDocente();
        sueldo.setDocente(docente);
        sueldo.setHorasTrabajadas(requestDTO.getHorasTrabajadas());
        sueldo.setPagoPorHora(requestDTO.getPagoPorHora());
        sueldo.setFechaPago(requestDTO.getFechaPago());

        // Regla de Negocio usando BigDecimal
        BigDecimal horas = BigDecimal.valueOf(requestDTO.getHorasTrabajadas());
        BigDecimal total = horas.multiply(requestDTO.getPagoPorHora());
        // Redondeo exacto a 2 decimales para base de datos
        total = total.setScale(2, java.math.RoundingMode.HALF_UP);

        sueldo.setSueldoTotal(total);
        sueldo.setEstado(true);

        SueldoDocente sueldoGuardado = sueldoRepository.save(sueldo);
        return mapearAResponse(sueldoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SueldoDocenteResponseDTO> listarPorDocente(Integer idDocente) {
        return sueldoRepository.findByDocente_IdDocenteAndEstadoTrue(idDocente)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    private SueldoDocenteResponseDTO mapearAResponse(SueldoDocente sueldo) {
        SueldoDocenteResponseDTO dto = new SueldoDocenteResponseDTO();
        dto.setIdSueldo(sueldo.getIdSueldo());
        dto.setIdDocente(sueldo.getDocente().getIdDocente());
        dto.setNombreCompletoDocente(sueldo.getDocente().getNombres() + " " + sueldo.getDocente().getApellidos());
        dto.setHorasTrabajadas(sueldo.getHorasTrabajadas());
        dto.setPagoPorHora(sueldo.getPagoPorHora());
        dto.setSueldoTotal(sueldo.getSueldoTotal());
        dto.setFechaPago(sueldo.getFechaPago());
        dto.setEstado(sueldo.getEstado());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public SueldoDocenteResponseDTO obtenerPorId(Integer id) {
        SueldoDocente sueldo = sueldoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de sueldo no encontrado con ID: " + id));
        return mapearAResponse(sueldo);
    }

    @Override
    @Transactional
    public SueldoDocenteResponseDTO actualizar(Integer id, SueldoDocenteRequestDTO requestDTO) {
        SueldoDocente sueldoExistente = sueldoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de sueldo no encontrado con ID: " + id));

        // Validamos si se corrigió el docente al que va el pago
        if (!sueldoExistente.getDocente().getIdDocente().equals(requestDTO.getIdDocente())) {
            Docente docente = docenteRepository.findById(requestDTO.getIdDocente())
                    .orElseThrow(() -> new RuntimeException("Docente no encontrado con ID: " + requestDTO.getIdDocente()));
            sueldoExistente.setDocente(docente);
        }

        // Actualizamos las horas, el pago por hora y la fecha
        sueldoExistente.setHorasTrabajadas(requestDTO.getHorasTrabajadas());
        sueldoExistente.setPagoPorHora(requestDTO.getPagoPorHora());
        sueldoExistente.setFechaPago(requestDTO.getFechaPago());

        // Regla de Negocio: Recalcular el Sueldo Total con BigDecimal
        java.math.BigDecimal horas = java.math.BigDecimal.valueOf(requestDTO.getHorasTrabajadas());
        java.math.BigDecimal total = horas.multiply(requestDTO.getPagoPorHora());
        total = total.setScale(2, java.math.RoundingMode.HALF_UP);
        sueldoExistente.setSueldoTotal(total);

        SueldoDocente sueldoActualizado = sueldoRepository.save(sueldoExistente);
        return mapearAResponse(sueldoActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        SueldoDocente sueldoExistente = sueldoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de sueldo no encontrado con ID: " + id));

        // Eliminación lógica (Soft Delete)
        sueldoExistente.setEstado(false);
        sueldoRepository.save(sueldoExistente);
    }
}