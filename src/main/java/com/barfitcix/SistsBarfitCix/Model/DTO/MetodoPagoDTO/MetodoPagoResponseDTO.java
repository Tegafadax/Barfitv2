package com.barfitcix.SistsBarfitCix.Model.DTO.MetodoPagoDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de MetodoPago (solo lectura)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetodoPagoResponseDTO {

    private Integer idMetodoPago;
    private String nomMetodo;
}
