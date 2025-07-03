// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SalaMesaDTO/SalaRequestDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SalaMesaDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para crear o actualizar una Sala
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaRequestDTO {

    @NotBlank(message = "El nombre de la sala es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nomSala;
}