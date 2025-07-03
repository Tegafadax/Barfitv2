package com.barfitcix.SistsBarfitCix.Model.DTO.LoginDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



// DTO para login request
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El email corporativo es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String emaCorporativo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
