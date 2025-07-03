// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SalaMesaDTO/SalaResponseDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SalaMesaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de una Sala
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaResponseDTO {

    private Integer idSala;
    private String nomSala;
}