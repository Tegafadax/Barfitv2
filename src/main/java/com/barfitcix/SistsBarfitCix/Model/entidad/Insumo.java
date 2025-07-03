package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "insumo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInsumo")
    private Integer idInsumo;

    @Column(name = "nomInsumo", nullable = false, unique = true, length = 100)
    private String nomInsumo;

    @Column(name = "idTipoCantidad", nullable = false)
    private Integer idTipoCantidad; // FK a tipoCantidad
}