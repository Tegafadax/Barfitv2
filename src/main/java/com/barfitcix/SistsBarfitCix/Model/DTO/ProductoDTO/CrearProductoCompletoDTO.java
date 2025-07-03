// CrearProductoCompletoDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para crear producto completo: nombre + precio base + insumos
 * El empleado se obtiene automáticamente del JWT
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearProductoCompletoDTO {

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nomProducto;

    @NotNull(message = "El precio inicial es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precioInicial;

    @NotEmpty(message = "Debe especificar al menos un insumo")
    @Valid
    private List<InsumoDetalleDTO> insumos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsumoDetalleDTO {

        @NotNull(message = "El ID del insumo es obligatorio")
        @Positive(message = "El ID del insumo debe ser positivo")
        private Integer idInsumo;

        @NotNull(message = "La cantidad por insumo es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        private Float cantPorInsumo;

        @NotNull(message = "El ID del tipo de cantidad es obligatorio")
        @Positive(message = "El ID del tipo de cantidad debe ser positivo")
        private Integer idTipoCantidad;
    }
}