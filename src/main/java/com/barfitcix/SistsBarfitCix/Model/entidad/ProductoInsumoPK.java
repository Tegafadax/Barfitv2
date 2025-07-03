package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductoInsumoPK implements Serializable {

    @Column(name = "idProducto")
    private Integer idProducto;

    @Column(name = "idInsumo")
    private Integer idInsumo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoInsumoPK that = (ProductoInsumoPK) o;
        return Objects.equals(idProducto, that.idProducto) &&
                Objects.equals(idInsumo, that.idInsumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idInsumo);
    }
}