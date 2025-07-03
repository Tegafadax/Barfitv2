// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/BoletaDTO/BoletaRequestDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.BoletaDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para crear una Boleta
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser un número positivo")
    private Integer idPedido;

    @NotNull(message = "El ID del método de pago es obligatorio")
    @Positive(message = "El ID del método de pago debe ser un número positivo")
    private Integer idMetodoPago;

    @NotNull(message = "El monto del pago es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto del pago debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El monto del pago debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal montoPago;

    @Size(max = 255, message = "El nombre del cliente no puede exceder 255 caracteres")
    private String nomCliente; // Opcional

    @Size(max = 8, message = "El DNI del cliente no puede exceder 8 caracteres")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe contener exactamente 8 dígitos numéricos")
    private String dniCliente; // Opcional y validación de formato

    // La fecha de pago se autogenera en la entidad, pero se podría enviar si se desea una específica
    // private LocalDateTime fecPago;
}