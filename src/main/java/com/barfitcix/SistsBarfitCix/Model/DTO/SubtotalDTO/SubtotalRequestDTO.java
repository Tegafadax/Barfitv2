// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SubtotalDTO/SubtotalRequestDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SubtotalDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para añadir un producto a un pedido (crear un registro de subtotal)
// y también para actualizar si los campos son opcionales
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtotalRequestDTO {

    // Se asume que idPedido y idProducto se obtendrán del path o serán combinados en un DTO de PedidoItem
    // Para esta clase, los trataremos como campos requeridos si se envía desde un body independiente.
    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser un número positivo")
    private Integer idPedido;

    @NotNull(message = "El ID del producto es obligatorio")
    @Positive(message = "El ID del producto debe ser un número positivo")
    private Integer idProducto;

    @NotNull(message = "La cantidad del producto es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantNumProd;

    @Size(max = 255, message = "El comentario no puede exceder 255 caracteres")
    private String comentario; // Opcional
}