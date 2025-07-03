// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SalaMesaDTO/MesaResponseDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SalaMesaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para respuesta de una Mesa (enriquecido con nombre de sala)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaResponseDTO {

    private Integer idMesa;
    private String codMesa;
    private String estado;
    private Integer posicionX;
    private Integer posicionY;
    private Integer idSala; // ID de la sala
    private String nomSala; // Nombre de la sala para visualización
}