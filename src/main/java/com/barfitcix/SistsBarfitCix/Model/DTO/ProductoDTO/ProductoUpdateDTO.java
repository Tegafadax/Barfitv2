package com.barfitcix.SistsBarfitCix.Model.DTO.ProductoDTO;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para actualizar producto
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoUpdateDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nomProducto;
}
