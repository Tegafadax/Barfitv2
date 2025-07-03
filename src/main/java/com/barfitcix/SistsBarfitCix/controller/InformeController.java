package com.barfitcix.SistsBarfitCix.controller;


import com.barfitcix.SistsBarfitCix.Model.DTO.InformeDTO.*;
import com.barfitcix.SistsBarfitCix.Model.Service.InformeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/informes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class InformeController {

    private final InformeService informeService;

    // Crear nuevo informe (Cualquier empleado autenticado)
    @PostMapping
    public ResponseEntity<?> crearInforme(@Valid @RequestBody InformeRequestDTO requestDTO) {
        try {
            InformeResponseDTO informeCreado = informeService.crearInforme(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Informe creado exitosamente",
                    "data", informeCreado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener todos los informes (Cualquier empleado autenticado)
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosInformes() {
        List<InformeResponseDTO> informes = informeService.obtenerTodosLosInformes();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Informes obtenidos exitosamente",
                "data", informes,
                "count", informes.size()
        ));
    }

    // Obtener informe por ID (Cualquier empleado autenticado)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInformePorId(@PathVariable Integer id) {
        return informeService.obtenerInformePorId(id)
                .map(informe -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Informe encontrado",
                        "data", informe
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Informe no encontrado con ID: " + id
                )));
    }

    // Obtener informes por año
    @GetMapping("/año/{año}")
    public ResponseEntity<Map<String, Object>> obtenerInformesPorAño(@PathVariable Integer año) {
        List<InformeResponseDTO> informes = informeService.obtenerInformesPorAño(año);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Informes del año " + año + " obtenidos exitosamente",
                "data", informes,
                "count", informes.size()
        ));
    }

    // Obtener informes por mes y año
    @GetMapping("/fecha/{mes}/{año}")
    public ResponseEntity<Map<String, Object>> obtenerInformesPorMesYAño(
            @PathVariable Integer mes,
            @PathVariable Integer año) {

        if (mes < 1 || mes > 12) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El mes debe estar entre 1 y 12"
            ));
        }

        List<InformeResponseDTO> informes = informeService.obtenerInformesPorMesYAño(mes, año);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Informes de " + mes + "/" + año + " obtenidos exitosamente",
                "data", informes,
                "count", informes.size()
        ));
    }

    // Obtener mis informes (empleado actual)
    @GetMapping("/mis-informes")
    public ResponseEntity<Map<String, Object>> obtenerMisInformes() {
        try {
            List<InformeResponseDTO> informes = informeService.obtenerMisInformes();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Tus informes obtenidos exitosamente",
                    "data", informes,
                    "count", informes.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener informes generados por un empleado específico (Solo ADMIN)
    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<Map<String, Object>> obtenerInformesPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            // Verificar permisos
            if (!informeService.esAdministrador()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "success", false,
                        "message", "Solo los administradores pueden ver informes de otros empleados"
                ));
            }

            List<InformeResponseDTO> informes = informeService.obtenerInformesPorEmpleado(idEmpleado);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Informes del empleado " + idEmpleado + " obtenidos exitosamente",
                    "data", informes,
                    "count", informes.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Buscar informes por nombre
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarInformesPorNombre(@RequestParam String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El texto de búsqueda es obligatorio"
            ));
        }

        List<InformeResponseDTO> informes = informeService.buscarInformesPorNombre(texto.trim());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Búsqueda completada para: " + texto,
                "data", informes,
                "count", informes.size()
        ));
    }

    // Buscar informes por rango de fechas
    @GetMapping("/buscar-fechas")
    public ResponseEntity<Map<String, Object>> buscarInformesPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        if (fechaInicio.isAfter(fechaFin)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "La fecha de inicio debe ser anterior a la fecha de fin"
            ));
        }

        List<InformeResponseDTO> informes = informeService.buscarInformesPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Informes en el rango de fechas obtenidos exitosamente",
                "data", informes,
                "count", informes.size(),
                "filtros", Map.of(
                        "fechaInicio", fechaInicio,
                        "fechaFin", fechaFin
                )
        ));
    }

    // Actualizar informe (Solo creador o ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInforme(
            @PathVariable Integer id,
            @Valid @RequestBody InformeUpdateDTO updateDTO) {
        try {
            InformeResponseDTO informeActualizado = informeService.actualizarInforme(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Informe actualizado exitosamente",
                    "data", informeActualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Eliminar informe (Solo creador o ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarInforme(@PathVariable Integer id) {
        try {
            boolean eliminado = informeService.eliminarInforme(id);
            if (eliminado) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Informe eliminado exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "No se pudo eliminar el informe"
                ));
            }
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("permisos") ?
                    HttpStatus.FORBIDDEN : HttpStatus.NOT_FOUND;
            return ResponseEntity.status(status).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Verificar información del usuario actual
    @GetMapping("/usuario-actual")
    public ResponseEntity<Map<String, Object>> verificarUsuarioActual() {
        try {
            boolean esAdmin = informeService.esAdministrador();
            Integer idEmpleado = informeService.obtenerIdEmpleadoAutenticado();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "idEmpleado", idEmpleado,
                            "esAdministrador", esAdmin,
                            "permisos", Map.of(
                                    "puedeCrearInformes", true,
                                    "puedeVerTodosLosInformes", true,
                                    "puedeVerInformesDeOtros", esAdmin,
                                    "puedeActualizarPropiosInformes", true,
                                    "puedeEliminarPropiosInformes", true,
                                    "puedeActualizarCualquierInforme", esAdmin,
                                    "puedeEliminarCualquierInforme", esAdmin
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
