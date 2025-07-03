// ProductoService.java (VERSIÓN MEJORADA)
package com.barfitcix.SistsBarfitCix.Model.Service;

import com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.entidad.*;
import com.barfitcix.SistsBarfitCix.Model.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoPrecioRepository productoPrecioRepository;
    private final ProductoInsumoRepository productoInsumoRepository;
    private final InsumoRepository insumoRepository;
    private final TipoCantidadRepository tipoCantidadRepository;
    private final AuthService authService;
    private final InsumoService insumoService;
    private final TipoCantidadService tipoCantidadService;

    // ===================== MÉTODO PRINCIPAL: CREAR PRODUCTO COMPLETO =====================

    /**
     * Crea un producto completo: nombre + precio base + insumos en una sola operación
     */
    public ProductoCompletoResponseDTO crearProductoCompleto(CrearProductoCompletoDTO dto) {
        try {
            // 1. Obtener empleado autenticado
            Empleado empleado = authService.obtenerEmpleadoAutenticado();
            log.info("Creando producto completo para empleado: {}", empleado.getNomEmpleado());

            // 2. Verificar nombre único
            if (productoRepository.existsByNomProducto(dto.getNomProducto().trim())) {
                throw new RuntimeException("Ya existe un producto con el nombre: " + dto.getNomProducto());
            }

            // 3. Validar todos los insumos ANTES de crear
            validarInsumos(dto.getInsumos());

            // 4. Crear y guardar el producto
            Producto producto = new Producto();
            producto.setNomProducto(dto.getNomProducto().trim());
            Producto productoGuardado = productoRepository.save(producto);

            // 5. Crear precio inicial
            ProductoPrecio precioInicial = ProductoPrecio.builder()
                    .idProducto(productoGuardado.getIdProducto())
                    .precio(dto.getPrecioInicial())
                    .idEmpleadoModifico(empleado.getIdEmpleado())
                    .activo(true)
                    .build();
            productoPrecioRepository.save(precioInicial);

            // 6. Crear insumos
            crearInsumos(productoGuardado.getIdProducto(), dto.getInsumos());

            log.info("Producto completo creado exitosamente: ID {}", productoGuardado.getIdProducto());

            // 7. Retornar producto completo
            return obtenerProductoCompleto(productoGuardado.getIdProducto());

        } catch (RuntimeException e) {
            log.error("Error al crear producto completo: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error interno al crear producto completo", e);
            throw new RuntimeException("Error interno al crear producto: " + e.getMessage(), e);
        }
    }

    // ===================== GESTIÓN DE PRECIOS =====================

    /**
     * Cambia el precio de un producto
     */
    public ProductoCompletoResponseDTO cambiarPrecio(CambiarPrecioDTO dto) {
        try {
            // Validar que el producto existe
            if (!productoRepository.existsById(dto.getIdProducto())) {
                throw new RuntimeException("Producto no encontrado con ID: " + dto.getIdProducto());
            }

            // Obtener empleado autenticado
            Empleado empleado = authService.obtenerEmpleadoAutenticado();

            // Validar que el precio sea diferente al actual
            Optional<ProductoPrecio> precioActualOpt = productoPrecioRepository
                    .findPrecioActivoByProducto(dto.getIdProducto());

            if (precioActualOpt.isPresent()) {
                BigDecimal precioActual = precioActualOpt.get().getPrecio();
                if (precioActual.compareTo(dto.getNuevoPrecio()) == 0) {
                    throw new RuntimeException("El nuevo precio es igual al precio actual");
                }
            }

            // Cerrar precio anterior
            LocalDateTime ahora = LocalDateTime.now();
            productoPrecioRepository.cerrarPrecioAnterior(dto.getIdProducto(), ahora);

            // Crear nuevo precio
            ProductoPrecio nuevoPrecio = ProductoPrecio.builder()
                    .idProducto(dto.getIdProducto())
                    .precio(dto.getNuevoPrecio())
                    .idEmpleadoModifico(empleado.getIdEmpleado())
                    .activo(true)
                    .build();
            productoPrecioRepository.save(nuevoPrecio);

            log.info("Precio cambiado para producto {}: {} -> {}",
                    dto.getIdProducto(),
                    precioActualOpt.map(pp -> pp.getPrecio()).orElse(BigDecimal.ZERO),
                    dto.getNuevoPrecio());

            return obtenerProductoCompleto(dto.getIdProducto());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar precio: " + e.getMessage(), e);
        }
    }

    // ===================== GESTIÓN DE INSUMOS =====================

    /**
     * Actualiza los insumos de un producto
     */
    public ProductoCompletoResponseDTO actualizarInsumos(ActualizarInsumosDTO dto) {
        try {
            // Validar que el producto existe
            if (!productoRepository.existsById(dto.getIdProducto())) {
                throw new RuntimeException("Producto no encontrado con ID: " + dto.getIdProducto());
            }

            // Validar nuevos insumos
            validarInsumos(dto.getInsumos());

            // Eliminar insumos existentes
            productoInsumoRepository.deleteByIdProducto(dto.getIdProducto());

            // Crear nuevos insumos
            crearInsumos(dto.getIdProducto(), dto.getInsumos());

            log.info("Insumos actualizados para producto {}: {} insumos",
                    dto.getIdProducto(), dto.getInsumos().size());

            return obtenerProductoCompleto(dto.getIdProducto());

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar insumos: " + e.getMessage(), e);
        }
    }

    // ===================== MÉTODOS DE CONSULTA =====================

    /**
     * Obtiene un producto completo por ID
     */
    @Transactional(readOnly = true)
    public Optional<ProductoCompletoResponseDTO> obtenerProductoCompletoPorId(Integer id) {
        if (!productoRepository.existsById(id)) {
            return Optional.empty();
        }
        return Optional.of(obtenerProductoCompleto(id));
    }

    /**
     * Lista productos con precio activo
     */
    @Transactional(readOnly = true)
    public List<ProductoCompletoResponseDTO> listarProductosDisponibles() {
        List<ProductoPrecio> preciosActivos = productoPrecioRepository.findByActivoTrueAndFechaFinIsNull();

        return preciosActivos.stream()
                .map(precio -> obtenerProductoCompleto(precio.getIdProducto()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el precio actual de un producto
     */
    @Transactional(readOnly = true)
    public BigDecimal obtenerPrecioActual(Integer idProducto) {
        return productoPrecioRepository.findPrecioActivoByProducto(idProducto)
                .map(ProductoPrecio::getPrecio)
                .orElse(BigDecimal.ZERO);
    }

    // ===================== MÉTODOS AUXILIARES =====================

    /**
     * Valida lista de insumos antes de crear/actualizar
     */
    private void validarInsumos(List<CrearProductoCompletoDTO.InsumoDetalleDTO> insumos) {
        for (CrearProductoCompletoDTO.InsumoDetalleDTO insumo : insumos) {
            // Validar que el insumo existe
            if (!insumoService.existeInsumoPorId(insumo.getIdInsumo())) {
                throw new RuntimeException("Insumo no encontrado con ID: " + insumo.getIdInsumo());
            }

            // Validar que el tipo de cantidad existe
            if (!tipoCantidadService.existePorId(insumo.getIdTipoCantidad())) {
                throw new RuntimeException("Tipo de cantidad no encontrado con ID: " + insumo.getIdTipoCantidad());
            }

            // Validar cantidad positiva
            if (insumo.getCantPorInsumo() <= 0) {
                throw new RuntimeException("La cantidad del insumo debe ser mayor a 0");
            }
        }

        // Validar que no hay insumos duplicados
        long insumosUnicos = insumos.stream()
                .map(CrearProductoCompletoDTO.InsumoDetalleDTO::getIdInsumo)
                .distinct()
                .count();

        if (insumosUnicos != insumos.size()) {
            throw new RuntimeException("No se pueden repetir insumos en la receta");
        }
    }

    /**
     * Crea los insumos para un producto
     */
    private void crearInsumos(Integer idProducto, List<CrearProductoCompletoDTO.InsumoDetalleDTO> insumos) {
        for (CrearProductoCompletoDTO.InsumoDetalleDTO insumoDTO : insumos) {
            ProductoInsumo productoInsumo = new ProductoInsumo(
                    idProducto,
                    insumoDTO.getIdInsumo(),
                    insumoDTO.getCantPorInsumo(),
                    insumoDTO.getIdTipoCantidad()
            );
            productoInsumoRepository.save(productoInsumo);
        }
    }

    /**
     * Obtiene producto completo (método central)
     */
    private ProductoCompletoResponseDTO obtenerProductoCompleto(Integer idProducto) {
        // Obtener producto básico
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductoCompletoResponseDTO dto = new ProductoCompletoResponseDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNomProducto(producto.getNomProducto());

        // Obtener precio actual
        Optional<ProductoPrecio> precioActual = productoPrecioRepository
                .findPrecioActivoByProducto(idProducto);

        if (precioActual.isPresent()) {
            ProductoPrecio precio = precioActual.get();
            dto.setPrecioActual(precio.getPrecio());
            dto.setFechaPrecioActual(precio.getFechaInicio());
            // Obtener nombre del empleado que modificó el precio
            dto.setEmpleadoModificoPrecio(
                    empleadoRepository.findById(precio.getIdEmpleadoModifico())
                            .map(Empleado::getNomEmpleado)
                            .orElse("Empleado no encontrado")
            );
        }

        // Obtener insumos
        List<ProductoInsumo> insumos = productoInsumoRepository.findByProductoWithDetails(idProducto);
        List<ProductoCompletoResponseDTO.ProductoInsumoDTO> insumosDTO = insumos.stream()
                .map(this::convertirInsumoADTO)
                .collect(Collectors.toList());
        dto.setInsumos(insumosDTO);

        return dto;
    }

    /**
     * Convierte ProductoInsumo a DTO
     */
    private ProductoCompletoResponseDTO.ProductoInsumoDTO convertirInsumoADTO(ProductoInsumo pi) {
        ProductoCompletoResponseDTO.ProductoInsumoDTO dto = new ProductoCompletoResponseDTO.ProductoInsumoDTO();
        dto.setIdInsumo(pi.getId().getIdInsumo());
        dto.setCantPorInsumo(pi.getCantPorInsumo());
        dto.setIdTipoCantidad(pi.getIdTipoCantidad());

        // Enriquecer con nombres
        if (pi.getInsumo() != null) {
            dto.setNomInsumo(pi.getInsumo().getNomInsumo());
        } else {
            dto.setNomInsumo(insumoService.obtenerNombreInsumoPorId(pi.getId().getIdInsumo()));
        }

        if (pi.getTipoCantidad() != null) {
            dto.setNomTipoCantidad(pi.getTipoCantidad().getNomCantidad());
        } else {
            dto.setNomTipoCantidad(tipoCantidadService.obtenerNombrePorId(pi.getIdTipoCantidad()));
        }

        return dto;
    }

    // ===================== MÉTODOS ORIGINALES (MANTENIDOS) =====================

    public ProductoResponseDTO crearProducto(ProductoRequestDTO requestDTO) {
        if (productoRepository.existsByNomProducto(requestDTO.getNomProducto())) {
            throw new RuntimeException("Ya existe un producto con este nombre: " + requestDTO.getNomProducto());
        }

        Producto producto = new Producto();
        producto.setNomProducto(requestDTO.getNomProducto().trim());
        Producto productoGuardado = productoRepository.save(producto);
        return convertirAResponseDTO(productoGuardado);
    }

    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id).map(this::convertirAResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAllByOrderByNomProductoAsc()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorNombre(String texto) {
        return productoRepository.findByNomProductoContainingIgnoreCaseOrderByNomProductoAsc(texto)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> obtenerProductoPorNombre(String nomProducto) {
        return productoRepository.findByNomProductoIgnoreCase(nomProducto)
                .map(this::convertirAResponseDTO);
    }

    public ProductoResponseDTO actualizarProducto(Integer id, ProductoUpdateDTO updateDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        if (updateDTO.getNomProducto() != null &&
                !updateDTO.getNomProducto().trim().equals(producto.getNomProducto())) {
            if (productoRepository.existsByNomProductoAndIdProductoNot(
                    updateDTO.getNomProducto().trim(), id)) {
                throw new RuntimeException("Ya existe otro producto con este nombre: " + updateDTO.getNomProducto());
            }
            producto.setNomProducto(updateDTO.getNomProducto().trim());
        }

        Producto productoActualizado = productoRepository.save(producto);
        return convertirAResponseDTO(productoActualizado);
    }

    @Transactional
    public boolean eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }

        try {
            // 1. Eliminar insumos del producto
            productoInsumoRepository.deleteByIdProducto(id);

            // 2. Eliminar precios del producto (cerrar historial)
            productoPrecioRepository.deleteByIdProducto(id);

            // 3. Verificar si tiene pedidos (subtotal)
            // TODO: Verificar tabla subtotal cuando esté implementada
            // if (subtotalRepository.existsByIdProducto(id)) {
            //     throw new RuntimeException("No se puede eliminar el producto porque tiene pedidos asociados");
            // }

            // 4. Eliminar el producto
            productoRepository.deleteById(id);

            log.info("Producto {} eliminado exitosamente con todas sus dependencias", id);
            return true;

        } catch (Exception e) {
            log.error("Error al eliminar producto {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public boolean existeProductoPorId(Integer id) {
        return productoRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existeProductoPorNombre(String nomProducto) {
        return productoRepository.existsByNomProducto(nomProducto);
    }

    @Transactional(readOnly = true)
    public String obtenerNombreProductoPorId(Integer id) {
        return productoRepository.findById(id)
                .map(Producto::getNomProducto)
                .orElse("Producto no encontrado");
    }

    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getIdProducto(),
                producto.getNomProducto()
        );
    }

    private final EmpleadoRepository empleadoRepository; // En ProductoService
}