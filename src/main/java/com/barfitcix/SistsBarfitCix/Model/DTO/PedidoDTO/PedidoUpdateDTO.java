// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/PedidoDTO/PedidoUpdateDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.PedidoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para actualizar el estado de un Pedido
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoUpdateDTO {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(pendiente|completado|cancelado)$", message = "El estado debe ser 'pendiente', 'completado' o 'cancelado'")
    private String estado;
}