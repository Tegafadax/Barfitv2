package com.barfitcix.SistsBarfitCix.Model.Service;


import com.barfitcix.SistsBarfitCix.Model.DTO.InformeDTO.*;
import com.barfitcix.SistsBarfitCix.Model.entidad.Informe;
import com.barfitcix.SistsBarfitCix.Model.entidad.Empleado;
import com.barfitcix.SistsBarfitCix.Model.Repository.InformeRepository;
import com.barfitcix.SistsBarfitCix.Model.Repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InformeService {

    private final InformeRepository informeRepository;
    private final EmpleadoRepository empleadoRepository;
    private final AuthService authService;

    // Crear nuevo informe
    public InformeResponseDTO crearInforme(InformeRequestDTO requestDTO) {
        // 🔐 Obtener empleado autenticado automáticamente
        Empleado empleadoAutenticado = authService.obtenerEmpleadoAutenticado();

        // Verificar que el nombre de informe no exista
        if (informeRepository.existsByNomInforme(requestDTO.getNomInforme())) {
            throw new RuntimeException("Ya existe un informe con este nombre: " + requestDTO.getNomInforme());
        }

        // Crear y configurar nuevo informe
        Informe informe = new Informe();
        informe.setNomInforme(requestDTO.getNomInforme());
        informe.setLinkInforme(requestDTO.getLinkInforme());
        informe.setFecInforme(LocalDateTime.now());

        // 🎯 TRACKING: Asignar empleado que generó el informe
        informe.setIdEmpleado(empleadoAutenticado.getIdEmpleado());

        Informe informeGuardado = informeRepository.save(informe);
        return convertirAResponseDTO(informeGuardado);
    }

    // Obtener informe por ID
    @Transactional(readOnly = true)
    public Optional<InformeResponseDTO> obtenerInformePorId(Integer id) {
        return informeRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    // Obtener todos los informes
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> obtenerTodosLosInformes() {
        return informeRepository.findAllByOrderByFecInformeDesc()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener informes por año
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> obtenerInformesPorAño(Integer año) {
        return informeRepository.findByAño(año)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener informes por mes y año
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> obtenerInformesPorMesYAño(Integer mes, Integer año) {
        return informeRepository.findByMesYAño(mes, año)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener informes generados por un empleado específico
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> obtenerInformesPorEmpleado(Integer idEmpleado) {
        return informeRepository.findByIdEmpleado(idEmpleado)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener mis informes (empleado actual)
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> obtenerMisInformes() {
        Empleado empleadoAutenticado = authService.obtenerEmpleadoAutenticado();
        return obtenerInformesPorEmpleado(empleadoAutenticado.getIdEmpleado());
    }

    // Buscar informes por texto en el nombre
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> buscarInformesPorNombre(String texto) {
        return informeRepository.findByNomInformeContainingIgnoreCaseOrderByFecInformeDesc(texto)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar informes por rango de fechas
    @Transactional(readOnly = true)
    public List<InformeResponseDTO> buscarInformesPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return informeRepository.findByFecInformeBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar informe
    public InformeResponseDTO actualizarInforme(Integer id, InformeUpdateDTO updateDTO) {
        // 🔐 Obtener empleado autenticado automáticamente
        Empleado empleadoAutenticado = authService.obtenerEmpleadoAutenticado();

        Informe informe = informeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado con ID: " + id));

        // Verificar que solo el creador o un admin puede actualizar
        if (!informe.getIdEmpleado().equals(empleadoAutenticado.getIdEmpleado()) &&
                empleadoAutenticado.getIdRol() != 1) {
            throw new RuntimeException("No tienes permisos para actualizar este informe");
        }

        // Verificar nombre único si se está actualizando
        if (updateDTO.getNomInforme() != null &&
                !updateDTO.getNomInforme().equals(informe.getNomInforme())) {
            if (informeRepository.existsByNomInformeAndIdInformeNot(
                    updateDTO.getNomInforme(), id)) {
                throw new RuntimeException("Ya existe otro informe con este nombre: " + updateDTO.getNomInforme());
            }
            informe.setNomInforme(updateDTO.getNomInforme());
        }

        // Actualizar campos no nulos
        if (updateDTO.getLinkInforme() != null) {
            informe.setLinkInforme(updateDTO.getLinkInforme());
        }

        Informe informeActualizado = informeRepository.save(informe);
        return convertirAResponseDTO(informeActualizado);
    }

    // Eliminar informe
    public boolean eliminarInforme(Integer id) {
        // 🔐 Obtener empleado autenticado automáticamente
        Empleado empleadoAutenticado = authService.obtenerEmpleadoAutenticado();

        Informe informe = informeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado con ID: " + id));

        // Verificar que solo el creador o un admin puede eliminar
        if (!informe.getIdEmpleado().equals(empleadoAutenticado.getIdEmpleado()) &&
                empleadoAutenticado.getIdRol() != 1) {
            throw new RuntimeException("No tienes permisos para eliminar este informe");
        }

        informeRepository.deleteById(id);
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
    private InformeResponseDTO convertirAResponseDTO(Informe informe) {
        InformeResponseDTO responseDTO = new InformeResponseDTO();
        responseDTO.setIdInforme(informe.getIdInforme());
        responseDTO.setNomInforme(informe.getNomInforme());
        responseDTO.setLinkInforme(informe.getLinkInforme());
        responseDTO.setFecInforme(informe.getFecInforme());
        responseDTO.setIdEmpleadoGenero(informe.getIdEmpleado());

        // Formatear fecha como en la imagen (ej: "01 - 2025")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM - yyyy");
        responseDTO.setFechaFormateada(informe.getFecInforme().format(formatter));

        // Obtener nombre del empleado que generó el informe
        empleadoRepository.findById(informe.getIdEmpleado())
                .ifPresent(empleado -> responseDTO.setNombreEmpleadoGenero(empleado.getNomEmpleado()));

        return responseDTO;
    }
}
