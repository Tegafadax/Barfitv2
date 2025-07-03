// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SalaMesaDTO/MesaRequestDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SalaMesaDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para crear una Mesa
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaRequestDTO {

    @NotBlank(message = "El código de la mesa es obligatorio")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codMesa;

    @NotBlank(message = "El estado de la mesa es obligatorio")
    @Pattern(regexp = "^(libre|ocupada|mantenimiento)$", message = "El estado debe ser 'libre', 'ocupada' o 'mantenimiento'")
    private String estado;

    @NotNull(message = "El ID de la sala es obligatorio")
    @Positive(message = "El ID de la sala debe ser un número positivo")
    private Integer idSala;

    // Posiciones X e Y pueden ser null si el default es 0 en DB, o no nulas si siempre se envían
    // Como la DB tiene DEFAULT 0, no las marcamos como @NotNull aquí
    private Integer posicionX;
    private Integer posicionY;
}