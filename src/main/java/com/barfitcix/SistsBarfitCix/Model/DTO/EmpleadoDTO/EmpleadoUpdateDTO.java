package com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para actualizar empleado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoUpdateDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nomEmpleado;

    @Email(message = "Debe ser un email válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    private String emaCorporativo;

    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
    private String contrasena;

    @Min(value = 1, message = "El rol debe ser 1 (ADMIN) o 2 (EMPLEADO)")
    @Max(value = 2, message = "El rol debe ser 1 (ADMIN) o 2 (EMPLEADO)")
    private Integer idRol;
}
