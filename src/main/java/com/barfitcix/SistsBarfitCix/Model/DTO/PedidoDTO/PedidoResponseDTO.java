// src/main/java/com/barfitcix/SistsBarfitCix/Model/DTO/PedidoDTO/PedidoResponseDTO.java
package com.barfitcix.SistsBarfitCix.Model.DTO.PedidoDTO;

import com.barfitcix.SistsBarfitCix.Model.DTO.SalaMesaDTO.MesaResponseDTO; // Reutilizamos este DTO para las mesas
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// DTO para la respuesta de un Pedido (enriquecido con mesas y empleado)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Integer idPedido;
    private LocalDateTime fechaCreacion;
    private String estado;
    private Integer idEmpleado;
    private String nombreEmpleado; // Nombre del empleado que creó el pedido

    // Lista de mesas asociadas al pedido
    private List<MesaPedidoDetailsDTO> mesasAsociadas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MesaPedidoDetailsDTO {
        private Integer idMesa;
        private String codMesa;
        private String estadoMesa; // Estado actual de la mesa (libre, ocupada, etc.)
        private Integer idSala;
        private String nomSala; // Nombre de la sala
    }
}