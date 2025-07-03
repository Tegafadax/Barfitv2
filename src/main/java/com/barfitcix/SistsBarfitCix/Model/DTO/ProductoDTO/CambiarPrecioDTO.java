package com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para cambiar precio de un producto
 * El empleado se obtiene automáticamente del JWT
 * El idProducto se toma del path parameter, no del body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambiarPrecioDTO {

    // ❌ QUITAR VALIDACIONES - Se asigna desde path parameter
    private Integer idProducto;

    @NotNull(message = "El nuevo precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal nuevoPrecio;

    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    private String motivo; // Opcional
}
