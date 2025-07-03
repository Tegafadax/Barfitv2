package com.barfitcix.SistsBarfitCix.Model.DTO.LoginDTO;

import com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO.EmpleadoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para login response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tipo; // "Bearer"
    private LocalDateTime expiracion;
    private EmpleadoResponseDTO empleado;
}
