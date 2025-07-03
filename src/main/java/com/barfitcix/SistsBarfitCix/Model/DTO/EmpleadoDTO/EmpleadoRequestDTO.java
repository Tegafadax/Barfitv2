package com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para crear empleado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoRequestDTO {

    @NotBlank(message = "El nombre del empleado es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nomEmpleado;

    @NotBlank(message = "El email corporativo es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    private String emaCorporativo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres")
    private String contrasena;

    @NotNull(message = "El rol es obligatorio")
    @Min(value = 1, message = "El rol debe ser 1 (ADMIN) o 2 (EMPLEADO)")
    @Max(value = 2, message = "El rol debe ser 1 (ADMIN) o 2 (EMPLEADO)")
    private Integer idRol;
}