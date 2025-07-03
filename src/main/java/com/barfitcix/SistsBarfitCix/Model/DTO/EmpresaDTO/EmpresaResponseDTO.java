package com.barfitcix.SistsBarfitCix.Model.DTO.EmpresaDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// DTO para respuesta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponseDTO {

    private Integer idInfoEmpresa;
    private String nomEmpresa;
    private String dirEmpresa;
    private String telEmpresa;
    private String corEmpresa;
    private String logoEmpresa;
    private LocalDateTime fecModificacionInfo;
    private String nombreEmpleadoModifico; // Nombre del empleado que modificó
    private Integer idEmpleadoModifico; // ID del empleado que modificó
}
