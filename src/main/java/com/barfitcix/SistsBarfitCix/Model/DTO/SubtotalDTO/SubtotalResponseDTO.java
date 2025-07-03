// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/SubtotalDTO/SubtotalResponseDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.SubtotalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// DTO para la respuesta de un registro de Subtotal (enriquecido con nombre de producto y cálculos)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubtotalResponseDTO {

    private Integer idPedido;
    private Integer idProducto;
    private String nombreProducto; // Nombre del producto
    private Integer cantNumProd;
    private String comentario;
    private BigDecimal precioUnitarioActual; // Precio actual del producto en el momento de la consulta
    private BigDecimal subtotalLinea; // cantNumProd * precioUnitarioActual
}