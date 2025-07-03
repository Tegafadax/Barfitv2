// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/PedidoDTO/PedidoRequestDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.PedidoDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// DTO para crear un Pedido, especialmente al unir mesas
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {

    @NotEmpty(message = "Debe especificar al menos una mesa para el pedido")
    private List<@NotNull(message = "El ID de mesa no puede ser nulo") @Positive(message = "El ID de mesa debe ser un número positivo") Integer> idMesas;

    // El idEmpleado se obtendrá del token JWT en el servicio, no se envía en el request
    // El estado inicial será 'pendiente' por defecto en la entidad o el servicio
}