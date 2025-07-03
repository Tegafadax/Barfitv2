// ProductoInsumo.java
package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productoInsumo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoInsumo {

    @EmbeddedId
    private ProductoInsumoPK id;

    @Column(name = "cantPorInsumo", nullable = false)
    private Float cantPorInsumo;

    @Column(name = "idTipoCantidad", nullable = false)
    private Integer idTipoCantidad;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idInsumo", insertable = false, updatable = false)
    private Insumo insumo;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER para tipo cantidad (tabla pequeña)
    @JoinColumn(name = "idTipoCantidad", insertable = false, updatable = false)
    private TipoCantidad tipoCantidad;

    // Constructor helper
    public ProductoInsumo(Integer idProducto, Integer idInsumo, Float cantPorInsumo, Integer idTipoCantidad) {
        this.id = new ProductoInsumoPK(idProducto, idInsumo);
        this.cantPorInsumo = cantPorInsumo;
        this.idTipoCantidad = idTipoCantidad;
    }
}