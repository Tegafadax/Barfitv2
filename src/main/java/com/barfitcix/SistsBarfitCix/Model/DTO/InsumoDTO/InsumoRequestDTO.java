package com.barfitcix.SistsBarfitCix.Model.DTO.InsumoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para crear insumo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequestDTO {

    @NotBlank(message = "El nombre del insumo es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nomInsumo;

    @NotNull(message = "El tipo de cantidad es obligatorio")
    @Positive(message = "El ID del tipo de cantidad debe ser un número positivo")
    private Integer idTipoCantidad;
}
