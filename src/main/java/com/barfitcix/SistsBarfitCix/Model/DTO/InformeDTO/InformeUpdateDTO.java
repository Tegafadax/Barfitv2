package com.barfitcix.SistsBarfitCix.Model.DTO.InformeDTO;


import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para actualizar informe
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformeUpdateDTO {

    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nomInforme;

    @Size(max = 255, message = "El enlace no puede exceder 255 caracteres")
    @Pattern(regexp = "^(https?://).*", message = "El enlace debe ser una URL válida (http:// o https://)")
    private String linkInforme;
}
