package com.barfitcix.SistsBarfitCix.controller;

import com.barfitcix.SistsBarfitCix.Model.DTO.MetodoPagoDTO.MetodoPagoResponseDTO;
import com.barfitcix.SistsBarfitCix.Model.Service.MetodoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/metodos-pago")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    // Obtener todos los métodos de pago (para dropdowns en Angular)
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        List<MetodoPagoResponseDTO> metodosPago = metodoPagoService.obtenerTodos();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Métodos de pago obtenidos exitosamente",
                "data", metodosPago,
                "count", metodosPago.size()
        ));
    }

    // Obtener método de pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        return metodoPagoService.obtenerPorId(id)
                .map(metodoPago -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Método de pago encontrado",
                        "data", metodoPago
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Método de pago no encontrado con ID: " + id
                )));
    }

    // Obtener método de pago por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerPorNombre(@PathVariable String nombre) {
        return metodoPagoService.obtenerPorNombre(nombre)
                .map(metodoPago -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Método de pago encontrado",
                        "data", metodoPago
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Método de pago no encontrado: " + nombre
                )));
    }

    // Buscar métodos de pago por texto
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorTexto(@RequestParam String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El texto de búsqueda es obligatorio"
            ));
        }

        List<MetodoPagoResponseDTO> resultados = metodoPagoService.buscarPorTexto(texto.trim());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Búsqueda completada para: " + texto,
                "data", resultados,
                "count", resultados.size()
        ));
    }

    // Verificar si existe un método de pago por ID (endpoint útil para validaciones)
    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Object>> verificarExistencia(@PathVariable Integer id) {
        boolean existe = metodoPagoService.existePorId(id);
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
        List<MetodoPagoResponseDTO> metodos = metodoPagoService.obtenerTodos();
        List<String> nombres = metodos.stream()
                .map(MetodoPagoResponseDTO::getNomMetodo)
                .toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Listado simple de métodos de pago",
                "data", nombres,
                "count", nombres.size()
        ));
    }
}