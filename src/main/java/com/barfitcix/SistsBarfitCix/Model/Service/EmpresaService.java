package com.barfitcix.SistsBarfitCix.Model.Service;



import com.barfitcix.SistsBarfitCix.Model.DTO.EmpresaDTO.*;
import com.barfitcix.SistsBarfitCix.Model.entidad.Empresa;
import com.barfitcix.SistsBarfitCix.Model.entidad.Empleado;
import com.barfitcix.SistsBarfitCix.Model.Repository.EmpresaRepository;
import com.barfitcix.SistsBarfitCix.Model.Repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final AuthService authService;

    // Crear nueva empresa
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO requestDTO) {
        // 🔐 Obtener empleado autenticado automáticamente
        Empleado empleadoAutenticado = authService.obtenerEmpleadoAutenticado();

        // Verificar que el nombre de empresa no exista
        if (empresaRepository.existsByNomEmpresa(requestDTO.getNomEmpresa())) {
            throw new RuntimeException("Ya existe una empresa con este nombre: " + requestDTO.getNomEmpresa());
        }

        // Crear y configurar nueva empresa
        Empresa empresa = new Empresa();
        empresa.setNomEmpresa(requestDTO.getNomEmpresa());
        empresa.setDirEmpresa(requestDTO.getDirEmpresa());
        empresa.setTelEmpresa(requestDTO.getTelEmpresa());
        empresa.setCorEmpresa(requestDTO.getCorEmpresa());
        empresa.setLogoEmpresa(requestDTO.getLogoEmpresa());
        empresa.setFecModificacionInfo(LocalDateTime.now());

        // 🎯 TRACKING: Asignar empleado que creó la empresa
        empresa.setIdEmpleado(empleadoAutenticado.getIdEmpleado());

        Empresa empresaGuardada = empresaRepository.save(empresa);
        return convertirAResponseDTO(empresaGuardada);
    }

    // Obtener empresa por ID
    @Transactional(readOnly = true)
    public Optional<EmpresaResponseDTO> obtenerEmpresaPorId(Integer id) {
        return empresaRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    // Obtener todas las empresas
    @Transactional(readOnly = true)
    public List<EmpresaResponseDTO> obtenerTodasLasEmpresas() {
        return empresaRepository.findAllByOrderByFecModificacionInfoDesc()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener empresas modificadas por un empleado específico
    @Transactional(readOnly = true)
    public List<EmpresaResponseDTO> obtenerEmpresasPorEmpleado(Integer idEmpleado) {
        return empresaRepository.findByIdEmpleado(idEmpleado)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar empresa
    public EmpresaResponseDTO actualizarEmpresa(Integer id, EmpresaUpdateDTO updateDTO) {
        // 🔐 Obtener empleado autenticado automáticamente
        Empleado empleadoAutenticado = authService.obtenerEmpleadoAutenticado();

        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada con ID: " + id));

        // Verificar nombre único si se está actualizando
        if (updateDTO.getNomEmpresa() != null &&
                !updateDTO.getNomEmpresa().equals(empresa.getNomEmpresa())) {
            if (empresaRepository.existsByNomEmpresaAndIdInfoEmpresaNot(
                    updateDTO.getNomEmpresa(), id)) {
                throw new RuntimeException("Ya existe otra empresa con este nombre: " + updateDTO.getNomEmpresa());
            }
            empresa.setNomEmpresa(updateDTO.getNomEmpresa());
        }

        // Actualizar campos no nulos
        if (updateDTO.getDirEmpresa() != null) {
            empresa.setDirEmpresa(updateDTO.getDirEmpresa());
        }

        if (updateDTO.getTelEmpresa() != null) {
            empresa.setTelEmpresa(updateDTO.getTelEmpresa());
        }

        if (updateDTO.getCorEmpresa() != null) {
            empresa.setCorEmpresa(updateDTO.getCorEmpresa());
        }

        if (updateDTO.getLogoEmpresa() != null) {
            empresa.setLogoEmpresa(updateDTO.getLogoEmpresa());
        }

        // 🎯 TRACKING: Actualizar empleado que modificó y fecha
        empresa.setIdEmpleado(empleadoAutenticado.getIdEmpleado());
        empresa.setFecModificacionInfo(LocalDateTime.now());

        Empresa empresaActualizada = empresaRepository.save(empresa);
        return convertirAResponseDTO(empresaActualizada);
    }

    // Eliminar empresa (eliminación física)
    public boolean eliminarEmpresa(Integer id) {
        if (!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa no encontrada con ID: " + id);
        }

        empresaRepository.deleteById(id);
        return true;
    }

    // Obtener ID del empleado autenticado (método auxiliar)
    public Integer obtenerIdEmpleadoAutenticado() {
        try {
            Empleado empleado = authService.obtenerEmpleadoAutenticado();
            return empleado.getIdEmpleado();
        } catch (RuntimeException e) {
            throw new RuntimeException("No hay empleado autenticado para realizar esta operación");
        }
    }

    // Verificar si el usuario actual es administrador
    public boolean esAdministrador() {
        try {
            Empleado empleado = authService.obtenerEmpleadoAutenticado();
            return empleado.getIdRol() == 1; // 1 = ADMIN
        } catch (RuntimeException e) {
            return false;
        }
    }

    // Método auxiliar para convertir Entity a ResponseDTO
    private EmpresaResponseDTO convertirAResponseDTO(Empresa empresa) {
        EmpresaResponseDTO responseDTO = new EmpresaResponseDTO();
        responseDTO.setIdInfoEmpresa(empresa.getIdInfoEmpresa());
        responseDTO.setNomEmpresa(empresa.getNomEmpresa());
        responseDTO.setDirEmpresa(empresa.getDirEmpresa());
        responseDTO.setTelEmpresa(empresa.getTelEmpresa());
        responseDTO.setCorEmpresa(empresa.getCorEmpresa());
        responseDTO.setLogoEmpresa(empresa.getLogoEmpresa());
        responseDTO.setFecModificacionInfo(empresa.getFecModificacionInfo());
        responseDTO.setIdEmpleadoModifico(empresa.getIdEmpleado());

        // Obtener nombre del empleado que modificó
        empleadoRepository.findById(empresa.getIdEmpleado())
                .ifPresent(empleado -> responseDTO.setNombreEmpleadoModifico(empleado.getNomEmpleado()));

        return responseDTO;
    }
}
