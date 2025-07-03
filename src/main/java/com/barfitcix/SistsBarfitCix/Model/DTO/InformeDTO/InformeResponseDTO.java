package com.barfitcix.SistsBarfitCix.Model.DTO.InformeDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para respuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformeResponseDTO {

    private Integer idInforme;
    private String nomInforme;
    private String linkInforme;
    private LocalDateTime fecInforme;
    private String nombreEmpleadoGenero; // Nombre del empleado que generó el informe
    private Integer idEmpleadoGenero; // ID del empleado que generó el informe
    private String fechaFormateada; // Fecha en formato amigable (ej: "01 - 2025")
}
