// ProductoController.java (VERSIÓN MEJORADA)
package com.barfitcix.SistsBarfitCix.controller;

import com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.Service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService productoService;

    // ===================== ENDPOINTS PRINCIPALES (ENFOQUE INTEGRAL) =====================

    /**
     * 🎯 ENDPOINT PRINCIPAL: Crear producto completo (nombre + precio + insumos)
     * Este es el enfoque correcto que evita productos "huérfanos"
     */
    @PostMapping("/crear-completo")
    public ResponseEntity<?> crearProductoCompleto(@Valid @RequestBody CrearProductoCompletoDTO requestDTO) {
        try {
            ProductoCompletoResponseDTO productoCreado = productoService.crearProductoCompleto(requestDTO);

            log.info("Producto completo creado exitosamente: {} (ID: {})",
                    productoCreado.getNomProducto(), productoCreado.getIdProducto());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Producto completo creado exitosamente",
                    "data", productoCreado
            ));
        } catch (RuntimeException e) {
            log.warn("Error al crear producto completo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error interno al crear producto completo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    /**
     * 💰 Cambiar precio de un producto existente
     */
    @PutMapping("/{id}/cambiar-precio")
    public ResponseEntity<?> cambiarPrecio(
            @PathVariable Integer id,
            @Valid @RequestBody CambiarPrecioDTO cambiarPrecioDTO) {
        try {
            // Asegurar que el ID del path coincida con el del DTO
            cambiarPrecioDTO.setIdProducto(id);

            ProductoCompletoResponseDTO productoActualizado = productoService.cambiarPrecio(cambiarPrecioDTO);

            log.info("Precio cambiado para producto {} a {}", id, cambiarPrecioDTO.getNuevoPrecio());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Precio actualizado exitosamente",
                    "data", productoActualizado,
                    "precioAnterior", productoService.obtenerPrecioActual(id),
                    "precioNuevo", cambiarPrecioDTO.getNuevoPrecio()
            ));
        } catch (RuntimeException e) {
            log.warn("Error al cambiar precio del producto {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error interno al cambiar precio del producto {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    /**
     * 🧪 Actualizar insumos (receta) de un producto
     */
    @PutMapping("/{id}/actualizar-insumos")
    public ResponseEntity<?> actualizarInsumos(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarInsumosDTO actualizarInsumosDTO) {
        try {
            // Asegurar que el ID del path coincida con el del DTO
            actualizarInsumosDTO.setIdProducto(id);

            ProductoCompletoResponseDTO productoActualizado = productoService.actualizarInsumos(actualizarInsumosDTO);

            log.info("Insumos actualizados para producto {}: {} insumos",
                    id, actualizarInsumosDTO.getInsumos().size());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Receta actualizada exitosamente",
                    "data", productoActualizado,
                    "insumosActualizados", actualizarInsumosDTO.getInsumos().size()
            ));
        } catch (RuntimeException e) {
            log.warn("Error al actualizar insumos del producto {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Error interno al actualizar insumos del producto {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    // ===================== CONSULTAS AVANZADAS =====================

    /**
     * 📋 Obtener producto completo por ID (con precio e insumos)
     */
    @GetMapping("/{id}/completo")
    public ResponseEntity<?> obtenerProductoCompleto(@PathVariable Integer id) {
        return productoService.obtenerProductoCompletoPorId(id)
                .map(producto -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Producto completo encontrado",
                        "data", producto
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Producto no encontrado con ID: " + id
                )));
    }

    /**
     * 🏪 Listar productos disponibles (solo con precio activo)
     */
    @GetMapping("/disponibles")
    public ResponseEntity<Map<String, Object>> obtenerProductosDisponibles() {
        try {
            List<ProductoCompletoResponseDTO> productos = productoService.listarProductosDisponibles();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Productos disponibles obtenidos exitosamente",
                    "data", productos,
                    "count", productos.size(),
                    "filtro", "Solo productos con precio activo"
            ));
        } catch (Exception e) {
            log.error("Error al obtener productos disponibles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    /**
     * 💲 Obtener precio actual de un producto
     */
    @GetMapping("/{id}/precio-actual")
    public ResponseEntity<Map<String, Object>> obtenerPrecioActual(@PathVariable Integer id) {
        try {
            if (!productoService.existeProductoPorId(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Producto no encontrado con ID: " + id
                ));
            }

            BigDecimal precio = productoService.obtenerPrecioActual(id);
            boolean disponible = precio.compareTo(BigDecimal.ZERO) > 0;

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", Map.of(
                            "idProducto", id,
                            "precioActual", precio,
                            "disponible", disponible,
                            "mensaje", disponible ? "Producto disponible" : "Producto sin precio activo"
                    )
            ));
        } catch (Exception e) {
            log.error("Error al obtener precio actual del producto {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }

    // ===================== ENDPOINTS ORIGINALES (MANTENIDOS) =====================

    /**
     * ⚠️ Crear producto básico (solo nombre) - NO RECOMENDADO
     * Se mantiene por compatibilidad pero genera productos sin precio
     */
    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody ProductoRequestDTO requestDTO) {
        try {
            ProductoResponseDTO productoCreado = productoService.crearProducto(requestDTO);

            log.warn("Producto básico creado (SIN precio): {} - Se recomienda usar /crear-completo",
                    productoCreado.getNomProducto());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Producto básico creado (sin precio)",
                    "warning", "Este producto no estará disponible hasta asignar un precio",
                    "data", productoCreado,
                    "recomendacion", "Usar /productos/crear-completo para productos completos"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtener todos los productos (básicos)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosProductos() {
        List<ProductoResponseDTO> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Productos obtenidos exitosamente",
                "data", productos,
                "count", productos.size(),
                "nota", "Para ver productos con precios usar /disponibles"
        ));
    }

    /**
     * Obtener producto por ID (básico)
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Integer id) {
        return productoService.obtenerProductoPorId(id)
                .map(producto -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Producto encontrado",
                        "data", producto,
                        "nota", "Para ver información completa usar /{id}/completo"
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Producto no encontrado con ID: " + id
                )));
    }

    /**
     * Obtener producto por nombre exacto
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerProductoPorNombre(@PathVariable String nombre) {
        return productoService.obtenerProductoPorNombre(nombre)
                .map(producto -> ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Producto encontrado",
                        "data", producto
                )))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "Producto no encontrado: " + nombre
                )));
    }

    /**
     * Buscar productos por texto en el nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarProductosPorNombre(@RequestParam String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "El texto de búsqueda es obligatorio"
            ));
        }

        List<ProductoResponseDTO> resultados = productoService.buscarProductosPorNombre(texto.trim());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Búsqueda completada para: " + texto,
                "data", resultados,
                "count", resultados.size()
        ));
    }

    /**
     * Actualizar producto (solo nombre)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoUpdateDTO updateDTO) {
        try {
            ProductoResponseDTO productoActualizado = productoService.actualizarProducto(id, updateDTO);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Producto actualizado exitosamente",
                    "data", productoActualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Eliminar producto
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarProducto(@PathVariable Integer id) {
        try {
            boolean eliminado = productoService.eliminarProducto(id);
            if (eliminado) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Producto eliminado exitosamente"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "success", false,
                        "message", "No se pudo eliminar el producto"
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Verificar si existe un producto por ID
     */
    @GetMapping("/{id}/existe")
    public ResponseEntity<Map<String, Object>> verificarExistencia(@PathVariable Integer id) {
        boolean existe = productoService.existeProductoPorId(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "id", id,
                        "existe", existe
                )
        ));
    }

    /**
     * Endpoint para obtener solo los nombres (útil para dropdowns)
     */
    @GetMapping("/listado-simple")
    public ResponseEntity<Map<String, Object>> obtenerListadoSimple() {
        List<ProductoResponseDTO> productos = productoService.obtenerTodosLosProductos();
        List<String> nombres = productos.stream()
                .map(ProductoResponseDTO::getNomProducto)
                .toList();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Listado simple de productos",
                "data", nombres,
                "count", nombres.size()
        ));
    }

    // ===================== ENDPOINTS DE ESTADÍSTICAS =====================

    /**
     * 📊 Estadísticas de productos
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        try {
            List<ProductoResponseDTO> todosProductos = productoService.obtenerTodosLosProductos();
            List<ProductoCompletoResponseDTO> productosDisponibles = productoService.listarProductosDisponibles();

            long totalProductos = todosProductos.size();
            long productosConPrecio = productosDisponibles.size();
            long productosSinPrecio = totalProductos - productosConPrecio;

            double porcentajeDisponibles = totalProductos > 0 ?
                    (productosConPrecio * 100.0 / totalProductos) : 0;

            double precioPromedio = productosDisponibles.stream()
                    .filter(p -> p.getPrecioActual() != null)
                    .mapToDouble(p -> p.getPrecioActual().doubleValue())
                    .average()
                    .orElse(0.0);

            Map<String, Object> estadisticas = Map.of(
                    "totalProductos", totalProductos,
                    "productosDisponibles", productosConPrecio,
                    "productosSinPrecio", productosSinPrecio,
                    "porcentajeDisponibles", Math.round(porcentajeDisponibles * 100.0) / 100.0,
                    "precioPromedio", Math.round(precioPromedio * 100.0) / 100.0,
                    "recomendacion", productosSinPrecio > 0 ?
                            "Hay " + productosSinPrecio + " productos sin precio activo" :
                            "Todos los productos tienen precio activo"
            );

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Estadísticas de productos",
                    "data", estadisticas
            ));
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de productos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "Error interno del servidor"
            ));
        }
    }
}