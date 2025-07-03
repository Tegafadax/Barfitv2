package com.barfitcix.SistsBarfitCix.Model.DTO.InsumoDTO;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para actualizar insumo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoUpdateDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nomInsumo;

    @Positive(message = "El ID del tipo de cantidad debe ser un número positivo")
    private Integer idTipoCantidad;
}
