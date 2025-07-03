package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipoCantidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCantidad {

    @Id
    @Column(name = "idTipoCantidad")
    private Integer idTipoCantidad;

    @Column(name = "nomCantidad", nullable = false, unique = true, length = 20)
    private String nomCantidad;
}