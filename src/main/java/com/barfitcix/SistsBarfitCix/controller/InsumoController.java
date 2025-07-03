package com.barfitcix.SistsBarfitCix.controller;

import com.barfitcix.SistsBarfitCix.Model.DTO.InsumoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.Service.InsumoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insumos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class InsumoController {

    private final InsumoService insumoService;

    // Crear nuevo insumo (Cualquier empleado autenticado)
    @PostMapping
    public ResponseEntity<?> crearInsumo(@Valid @RequestBody InsumoRequestDTO requestDTO) {
        try {
            InsumoResponseDTO insumoCreado = insumoService.crearInsumo(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Insumo creado exitosamente",
                    "data", insumoCreado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Obtener todos los insumos (Cualquier empleado autenticado)
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosInsumos() {
        List<InsumoResponseDTO> insumos = insumoService.obtenerTodosLosInsumos();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Insumos obtenidos exitosamente",
                "data", insumos,
                "count", insumos.size()
        ));
    }

    // Obtener insumo por ID (Cualquier empleado autenticado)
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerInsumoPorId(@PathVariable Integer id) {
        return insumoService.obtenerInsumoPorId(id)
                .map(insumo -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Insumo encontrado",
                        "data", insumo
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Insumo no encontrado con ID: " + id
                )));
    }

    // Obtener insumo por nombre exacto
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerInsumoPorNombre(@PathVariable String nombre) {
        return insumoService.obtenerInsumoPorNombre(nombre)
                .map(insumo -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Insumo encontrado",
                        "data", insumo
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Insumo no encontrado: " + nombre
                )));
    }

    // Buscar insumos por texto en el nombre
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarInsumosPorNombre(@RequestParam String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El texto de búsqueda es obligatorio"
            ));
        }

        List<InsumoResponseDTO> resultados = insumoService.buscarInsumosPorNombre(texto.trim());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Búsqueda completada para: " + texto,
                "data", resultados,
                "count", resultados.size()
        ));
    }

    // Obtener insumos por tipo de cantidad
    @GetMapping("/tipo-cantidad/{idTipoCantidad}")
    public ResponseEntity<?> obtenerInsumosPorTipoCantidad(@PathVariable Integer idTipoCantidad) {
        try {
            List<InsumoResponseDTO> insumos = insumoService.obtenerInsumosPorTipoCantidad(idTipoCantidad);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Insumos por tipo de cantidad obtenidos exitosamente",
                    "data", insumos,
                    "count", insumos.size(),
                    "filtro", Map.of("idTipoCantidad", idTipoCantidad)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Actualizar insumo (Cualquier empleado autenticado)
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarInsumo(
            @PathVariable Integer id,
            @Valid @RequestBody InsumoUpdateDTO updateDTO) {
        try {
            InsumoResponseDTO insumoActualizado = insumoService.actualizarInsumo(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Insumo actualizado exitosamente",
                    "data", insumoActualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Eliminar insumo (Cualquier empleado autenticado)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarInsumo(@PathVariable Integer id) {
        try {
            boolean eliminado = insumoService.eliminarInsumo(id);
            if (eliminado) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Insumo eliminado exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "No se pudo eliminar el insumo"
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Verificar si existe un insumo por ID (endpoint útil para validaciones)
    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Object>> verificarExistencia(@PathVariable Integer id) {
        boolean existe = insumoService.existeInsumoPorId(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "id", id,
                        "existe", existe
                )
        ));
    }

    // Contar insumos por tipo de cantidad
    @GetMapping("/contar/tipo-cantidad/{idTipoCantidad}")
    public ResponseEntity<Map<String, Object>> contarInsumosPorTipoCantidad(@PathVariable Integer idTipoCantidad) {
        try {
            long count = insumoService.contarInsumosPorTipoCantidad(idTipoCantidad);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "idTipoCantidad", idTipoCantidad,
                            "cantidadInsumos", count
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Endpoint para obtener solo los nombres (útil para dropdowns)
    @GetMapping("/listado-simple")
    public ResponseEntity<Map<String, Object>> obtenerListadoSimple() {
        List<InsumoResponseDTO> insumos = insumoService.obtenerTodosLosInsumos();
        List<String> nombres = insumos.stream()
                .map(InsumoResponseDTO::getNomInsumo)
                .toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Listado simple de insumos",
                "data", nombres,
                "count", nombres.size()
        ));
    }
}