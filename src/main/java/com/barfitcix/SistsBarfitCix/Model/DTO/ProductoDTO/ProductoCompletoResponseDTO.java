// ProductoCompletoResponseDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de respuesta para producto completo con precio e insumos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCompletoResponseDTO {

    private Integer idProducto;
    private String nomProducto;

    // Información del precio actual
    private BigDecimal precioActual;
    private LocalDateTime fechaPrecioActual;
    private String empleadoModificoPrecio;

    // Lista de insumos
    private List<ProductoInsumoDTO> insumos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoInsumoDTO {
        private Integer idInsumo;
        private String nomInsumo;
        private Float cantPorInsumo;
        private Integer idTipoCantidad;
        private String nomTipoCantidad;
    }
}