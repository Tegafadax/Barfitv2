// ActualizarInsumosDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para actualizar solo los insumos de un producto
 * El idProducto se toma del path parameter, no del body
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarInsumosDTO {

    // ❌ QUITAR VALIDACIONES - Se asigna desde path parameter
    private Integer idProducto;

    @NotEmpty(message = "Debe especificar al menos un insumo")
    @Valid
    private List<CrearProductoCompletoDTO.InsumoDetalleDTO> insumos;
}