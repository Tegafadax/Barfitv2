package com.barfitcix.SistsBarfitCix.Model.Service;


import com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.entidad.Empleado;
import com.barfitcix.SistsBarfitCix.Model.Repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PasswordEncoder passwordEncoder;

    // Crear nuevo empleado
    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO requestDTO) {
        // Verificar que el email no exista
        if (empleadoRepository.existsByEmaCorporativo(requestDTO.getEmaCorporativo())) {
            throw new RuntimeException("Ya existe un empleado con este email corporativo");
        }

        // Crear y configurar nuevo empleado
        Empleado empleado = new Empleado();
        empleado.setNomEmpleado(requestDTO.getNomEmpleado());
        empleado.setEmaCorporativo(requestDTO.getEmaCorporativo());
        empleado.setContrasena(passwordEncoder.encode(requestDTO.getContrasena()));
        empleado.setIdRol(requestDTO.getIdRol());
        empleado.setActivo(true);
        empleado.setFecIngreso(LocalDateTime.now());

        Empleado empleadoGuardado = empleadoRepository.save(empleado);
        return convertirAResponseDTO(empleadoGuardado);
    }

    // Obtener empleado por ID
    @Transactional(readOnly = true)
    public Optional<EmpleadoResponseDTO> obtenerEmpleadoPorId(Integer id) {
        return empleadoRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    // Obtener todos los empleados
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerTodosLosEmpleados() {
        return empleadoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener solo empleados activos
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerEmpleadosActivos() {
        return empleadoRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener empleados por rol
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> obtenerEmpleadosPorRol(Integer idRol) {
        return empleadoRepository.findByIdRol(idRol)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualizar empleado
    public EmpleadoResponseDTO actualizarEmpleado(Integer id, EmpleadoUpdateDTO updateDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        // Verificar email único si se está actualizando
        if (updateDTO.getEmaCorporativo() != null &&
                !updateDTO.getEmaCorporativo().equals(empleado.getEmaCorporativo())) {
            if (empleadoRepository.existsByEmaCorporativoAndIdEmpleadoNot(
                    updateDTO.getEmaCorporativo(), id)) {
                throw new RuntimeException("Ya existe otro empleado con este email corporativo");
            }
            empleado.setEmaCorporativo(updateDTO.getEmaCorporativo());
        }

        // Actualizar campos no nulos
        if (updateDTO.getNomEmpleado() != null) {
            empleado.setNomEmpleado(updateDTO.getNomEmpleado());
        }

        if (updateDTO.getContrasena() != null) {
            empleado.setContrasena(passwordEncoder.encode(updateDTO.getContrasena()));
        }

        if (updateDTO.getIdRol() != null) {
            empleado.setIdRol(updateDTO.getIdRol());
        }

        Empleado empleadoActualizado = empleadoRepository.save(empleado);
        return convertirAResponseDTO(empleadoActualizado);
    }

    // Desactivar empleado (eliminación lógica)
    public boolean desactivarEmpleado(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }

        int filasAfectadas = empleadoRepository.desactivarEmpleado(id, LocalDateTime.now());
        return filasAfectadas > 0;
    }

    // Reactivar empleado
    public boolean reactivarEmpleado(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con ID: " + id);
        }

        int filasAfectadas = empleadoRepository.reactivarEmpleado(id);
        return filasAfectadas > 0;
    }

    // Método auxiliar para convertir Entity a ResponseDTO
    private EmpleadoResponseDTO convertirAResponseDTO(Empleado empleado) {
        EmpleadoResponseDTO responseDTO = new EmpleadoResponseDTO();
        responseDTO.setIdEmpleado(empleado.getIdEmpleado());
        responseDTO.setNomEmpleado(empleado.getNomEmpleado());
        responseDTO.setEmaCorporativo(empleado.getEmaCorporativo());
        responseDTO.setFecIngreso(empleado.getFecIngreso());
        responseDTO.setFecSalida(empleado.getFecSalida());
        responseDTO.setActivo(empleado.getActivo());
        responseDTO.setIdRol(empleado.getIdRol());

        // Mapear nombre del rol
        responseDTO.setNombreRol(empleado.getIdRol() == 1 ? "ADMIN" : "EMPLEADO");

        return responseDTO;
    }
    // Agregar en EmpleadoService.java
    @Transactional(readOnly = true)
    public String obtenerNombreEmpleadoPorId(Integer id) {
        return empleadoRepository.findById(id)
                .map(Empleado::getNomEmpleado)
                .orElse("Empleado no encontrado");
    }
}