package com.barfitcix.SistsBarfitCix.Model.DTO.LoginDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para logout response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponseDTO {

    private String mensaje;
    private LocalDateTime fechaLogout;
}
