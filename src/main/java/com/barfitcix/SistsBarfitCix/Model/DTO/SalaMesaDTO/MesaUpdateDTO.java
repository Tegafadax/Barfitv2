// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SalaMesaDTO/MesaUpdateDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SalaMesaDTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para actualizar una Mesa
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaUpdateDTO {

    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codMesa;

    @Pattern(regexp = "^(libre|ocupada|mantenimiento)$", message = "El estado debe ser 'libre', 'ocupada' o 'mantenimiento'")
    private String estado;

    // El cambio de sala de una mesa es una operación más compleja, se manejaría en el servicio
    // Por ahora, no se incluye el idSala directamente en el updateDTO
    // @Positive(message = "El ID de la sala debe ser un número positivo")
    // private Integer idSala;

    private Integer posicionX;
    private Integer posicionY;
}