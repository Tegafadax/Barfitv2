    package com.barfitcix.SistsBarfitCix.Model.DTO.EmpresaDTO;

    import jakarta.validation.constraints.Email;

    import jakarta.validation.constraints.Size;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;


    // DTO para actualizar empresa
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class EmpresaUpdateDTO {

        @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
        private String nomEmpresa;

        @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
        private String dirEmpresa;

        @Size(max = 50, message = "El teléfono no puede exceder 50 caracteres")
        private String telEmpresa;

        @Email(message = "Debe ser un email válido")
        @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
        private String corEmpresa;

        @Size(max = 255, message = "La URL del logo no puede exceder 255 caracteres")
        private String logoEmpresa;
    }
