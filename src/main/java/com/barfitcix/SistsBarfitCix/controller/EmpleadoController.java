package com.barfitcix.SistsBarfitCix.controller;

import com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.Service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/empleados")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    // Crear nuevo empleado
    @PostMapping
    public ResponseEntity<?> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO requestDTO) {
        try {
            EmpleadoResponseDTO empleadoCreado = empleadoService.crearEmpleado(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Empleado creado exitosamente",
                    "data", empleadoCreado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener todos los empleados
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosEmpleados() {
        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerTodosLosEmpleados();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empleados obtenidos exitosamente",
                "data", empleados,
                "count", empleados.size()
        ));
    }

    // Obtener solo empleados activos
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerEmpleadosActivos() {
        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerEmpleadosActivos();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empleados activos obtenidos exitosamente",
                "data", empleados,
                "count", empleados.size()
        ));
    }

    // Obtener empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEmpleadoPorId(@PathVariable Integer id) {
        return empleadoService.obtenerEmpleadoPorId(id)
                .map(empleado -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Empleado encontrado",
                        "data", empleado
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Empleado no encontrado con ID: " + id
                )));
    }

    // Obtener empleados por rol
    @GetMapping("/rol/{idRol}")
    public ResponseEntity<Map<String, Object>> obtenerEmpleadosPorRol(@PathVariable Integer idRol) {
        if (idRol != 1 && idRol != 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El rol debe ser 1 (ADMIN) o 2 (EMPLEADO)"
            ));
        }

        List<EmpleadoResponseDTO> empleados = empleadoService.obtenerEmpleadosPorRol(idRol);
        String nombreRol = idRol == 1 ? "ADMIN" : "EMPLEADO";

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empleados con rol " + nombreRol + " obtenidos exitosamente",
                "data", empleados,
                "count", empleados.size()
        ));
    }

    // Actualizar empleado
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEmpleado(
            @PathVariable Integer id,
            @Valid @RequestBody EmpleadoUpdateDTO updateDTO) {
        try {
            EmpleadoResponseDTO empleadoActualizado = empleadoService.actualizarEmpleado(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Empleado actualizado exitosamente",
                    "data", empleadoActualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Desactivar empleado (eliminación lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> desactivarEmpleado(@PathVariable Integer id) {
        try {
            boolean desactivado = empleadoService.desactivarEmpleado(id);
            if (desactivado) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Empleado desactivado exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "No se pudo desactivar el empleado"
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Reactivar empleado
    @PatchMapping("/{id}/reactivar")
    public ResponseEntity<Map<String, Object>> reactivarEmpleado(@PathVariable Integer id) {
        try {
            boolean reactivado = empleadoService.reactivarEmpleado(id);
            if (reactivado) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Empleado reactivado exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "No se pudo reactivar el empleado"
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}