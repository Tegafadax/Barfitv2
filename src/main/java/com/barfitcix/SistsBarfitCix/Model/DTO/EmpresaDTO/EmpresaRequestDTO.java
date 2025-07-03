package com.barfitcix.SistsBarfitCix.Model.DTO.EmpresaDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// DTO para crear empresa
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String nomEmpresa;

    @NotBlank(message = "La dirección de la empresa es obligatoria")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String dirEmpresa;

    @NotBlank(message = "El teléfono de la empresa es obligatorio")
    @Size(max = 50, message = "El teléfono no puede exceder 50 caracteres")
    private String telEmpresa;

    @NotBlank(message = "El correo de la empresa es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    private String corEmpresa;

    @NotBlank(message = "El logo de la empresa es obligatorio")
    @Size(max = 255, message = "La URL del logo no puede exceder 255 caracteres")
    private String logoEmpresa;
}

