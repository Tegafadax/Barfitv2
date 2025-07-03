package com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {

    private Integer idProducto;
    private String nomProducto;
}
