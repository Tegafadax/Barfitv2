package com.barfitcix.SistsBarfitCix.controller;


import com.barfitcix.SistsBarfitCix.Model.DTO.EmpresaDTO.*;
import com.barfitcix.SistsBarfitCix.Model.Service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmpresaController {

    private final EmpresaService empresaService;

    // Crear nueva empresa (Solo ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearEmpresa(@Valid @RequestBody EmpresaRequestDTO requestDTO) {
        try {
            EmpresaResponseDTO empresaCreada = empresaService.crearEmpresa(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Empresa creada exitosamente",
                    "data", empresaCreada
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener todas las empresas (Cualquier empleado autenticado)
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasEmpresas() {
        List<EmpresaResponseDTO> empresas = empresaService.obtenerTodasLasEmpresas();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empresas obtenidas exitosamente",
                "data", empresas,
                "count", empresas.size()
        ));
    }

    // Obtener empresa por ID (Cualquier empleado autenticado)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEmpresaPorId(@PathVariable Integer id) {
        return empresaService.obtenerEmpresaPorId(id)
                .map(empresa -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Empresa encontrada",
                        "data", empresa
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Empresa no encontrada con ID: " + id
                )));
    }

    // Obtener empresas modificadas por empleado actual
    @GetMapping("/mis-modificaciones")
    public ResponseEntity<Map<String, Object>> obtenerMisModificaciones() {
        try {
            Integer idEmpleado = empresaService.obtenerIdEmpleadoAutenticado();
            List<EmpresaResponseDTO> empresas = empresaService.obtenerEmpresasPorEmpleado(idEmpleado);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Empresas modificadas por ti obtenidas exitosamente",
                    "data", empresas,
                    "count", empresas.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener empresas modificadas por un empleado específico (Solo ADMIN)
    @GetMapping("/empleado/{idEmpleado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtenerEmpresasPorEmpleado(@PathVariable Integer idEmpleado) {
        List<EmpresaResponseDTO> empresas = empresaService.obtenerEmpresasPorEmpleado(idEmpleado);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Empresas modificadas por empleado " + idEmpleado + " obtenidas exitosamente",
                "data", empresas,
                "count", empresas.size()
        ));
    }

    // Actualizar empresa (Solo ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarEmpresa(
            @PathVariable Integer id,
            @Valid @RequestBody EmpresaUpdateDTO updateDTO) {
        try {
            EmpresaResponseDTO empresaActualizada = empresaService.actualizarEmpresa(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Empresa actualizada exitosamente",
                    "data", empresaActualizada
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Eliminar empresa (Solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> eliminarEmpresa(@PathVariable Integer id) {
        try {
            boolean eliminada = empresaService.eliminarEmpresa(id);
            if (eliminada) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Empresa eliminada exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "No se pudo eliminar la empresa"
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Verificar información del usuario actual
    @GetMapping("/usuario-actual")
    public ResponseEntity<Map<String, Object>> verificarUsuarioActual() {
        try {
            boolean esAdmin = empresaService.esAdministrador();
            Integer idEmpleado = empresaService.obtenerIdEmpleadoAutenticado();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "idEmpleado", idEmpleado,
                            "esAdministrador", esAdmin,
                            "permisos", Map.of(
                                    "puedeCrearEmpresas", esAdmin,
                                    "puedeActualizarEmpresas", esAdmin,
                                    "puedeEliminarEmpresas", esAdmin,
                                    "puedeVerEmpresas", true
                            )
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
