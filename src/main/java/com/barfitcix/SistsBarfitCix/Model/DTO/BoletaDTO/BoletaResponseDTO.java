// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/BoletaDTO/BoletaResponseDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.BoletaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para la respuesta de una Boleta (enriquecido con nombres y totales)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaResponseDTO {

    private Integer idBoleta;
    private LocalDateTime fecBoleta;
    private String nomCliente;
    private String dniCliente;
    private LocalDateTime fecPago;
    private BigDecimal montoPago;

    private Integer idMetodoPago;
    private String nomMetodoPago; // Nombre descriptivo del método de pago

    private Integer idEmpleado;
    private String nombreEmpleado; // Nombre del empleado que generó la boleta

    private Integer idPedido;
    private String estadoPedido; // Estado actual del pedido asociado
    private BigDecimal totalCalculadoPedido; // Suma total de los subtotales del pedido
}