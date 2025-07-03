package com.barfitcix.SistsBarfitCix.Model.DTO.TipoCantidadDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de TipoCantidad (solo lectura)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCantidadResponseDTO {

    private Integer idTipoCantidad;
    private String nomCantidad;
}