package com.barfitcix.SistsBarfitCix.Model.DTO.InsumoDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para respuesta (enriquecido con nombre del tipo de cantidad)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoResponseDTO {

    private Integer idInsumo;
    private String nomInsumo;
    private Integer idTipoCantidad;
    private String nomTipoCantidad; // Nombre descriptivo del tipo de cantidad (ej: "Kg", "Lt")
}
