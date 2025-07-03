package com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para respuesta (sin contraseña)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoResponseDTO {

    private Integer idEmpleado;
    private String nomEmpleado;
    private String emaCorporativo;
    private LocalDateTime fecIngreso;
    private LocalDateTime fecSalida;
    private Boolean activo;
    private Integer idRol;
    private String nombreRol; // Para mostrar "ADMIN" o "EMPLEADO"
}