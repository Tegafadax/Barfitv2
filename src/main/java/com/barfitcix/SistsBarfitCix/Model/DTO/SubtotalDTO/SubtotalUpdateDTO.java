// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SubtotalDTO/SubtotalUpdateDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SubtotalDTO;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para actualizar la cantidad o comentario de un producto en un subtotal existente
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtotalUpdateDTO {

    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantNumProd;

    @Size(max = 255, message = "El comentario no puede exceder 255 caracteres")
    private String comentario;
}