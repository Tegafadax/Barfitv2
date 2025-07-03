package com.barfitcix.SistsBarfitCix.controller;


import com.barfitcix.SistsBarfitCix.Model.DTO.TipoCantidadDTO.TipoCantidadResponseDTO;
import com.barfitcix.SistsBarfitCix.Model.Service.TipoCantidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tipos-cantidad")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TipoCantidadController {

    private final TipoCantidadService tipoCantidadService;

    // Obtener todos los tipos de cantidad (para dropdowns en Angular)
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        List<TipoCantidadResponseDTO> tiposCantidad = tipoCantidadService.obtenerTodos();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Tipos de cantidad obtenidos exitosamente",
                "data", tiposCantidad,
                "count", tiposCantidad.size()
        ));
    }

    // Obtener tipo de cantidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return tipoCantidadService.obtenerPorId(id)
                .map(tipoCantidad -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Tipo de cantidad encontrado",
                        "data", tipoCantidad
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Tipo de cantidad no encontrado con ID: " + id
                )));
    }

    // Obtener tipo de cantidad por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerPorNombre(@PathVariable String nombre) {
        return tipoCantidadService.obtenerPorNombre(nombre)
                .map(tipoCantidad -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Tipo de cantidad encontrado",
                        "data", tipoCantidad
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Tipo de cantidad no encontrado: " + nombre
                )));
    }

    // Buscar tipos de cantidad por texto
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorTexto(@RequestParam String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El texto de búsqueda es obligatorio"
            ));
        }

        List<TipoCantidadResponseDTO> resultados = tipoCantidadService.buscarPorTexto(texto.trim());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Búsqueda completada para: " + texto,
                "data", resultados,
                "count", resultados.size()
        ));
    }

    // Verificar si existe un tipo de cantidad por ID (endpoint útil para validaciones)
    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Object>> verificarExistencia(@PathVariable Integer id) {
        boolean existe = tipoCantidadService.existePorId(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "id", id,
                        "existe", existe
                )
        ));
    }

    // Endpoint para obtener solo los nombres (útil para validaciones rápidas)
    @GetMapping("/listado-simple")
    public ResponseEntity<Map<String, Object>> obtenerListadoSimple() {
        List<TipoCantidadResponseDTO> tipos = tipoCantidadService.obtenerTodos();
        List<String> nombres = tipos.stream()
                .map(TipoCantidadResponseDTO::getNomCantidad)
                .toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Listado simple de tipos de cantidad",
                "data", nombres,
                "count", nombres.size()
        ));
    }
}
