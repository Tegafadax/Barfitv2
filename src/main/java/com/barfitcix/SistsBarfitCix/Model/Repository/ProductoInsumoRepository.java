package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.ProductoInsumo;
import com.barfitcix.SistsBarfitCix.Model.entidad.ProductoInsumoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoInsumoRepository extends JpaRepository<ProductoInsumo, ProductoInsumoPK> {

    // Obtener todos los insumos de un producto
    @Query("SELECT pi FROM ProductoInsumo pi " +
            "LEFT JOIN FETCH pi.insumo " +
            "LEFT JOIN FETCH pi.tipoCantidad " +
            "WHERE pi.id.idProducto = :idProducto")
    List<ProductoInsumo> findByProductoWithDetails(@Param("idProducto") Integer idProducto);

    // Obtener insumos básicos de un producto
    List<ProductoInsumo> findByIdIdProducto(Integer idProducto);

    // Eliminar todos los insumos de un producto
    @Modifying
    @Query("DELETE FROM ProductoInsumo pi WHERE pi.id.idProducto = :idProducto")
    void deleteByIdProducto(@Param("idProducto") Integer idProducto);

    // Verificar si un producto tiene insumos
    boolean existsByIdIdProducto(Integer idProducto);

    // Contar insumos de un producto
    long countByIdIdProducto(Integer idProducto);

    // Verificar si un insumo está siendo usado
    boolean existsByIdIdInsumo(Integer idInsumo);

    // Obtener productos que usan un insumo específico
    @Query("SELECT DISTINCT pi.id.idProducto FROM ProductoInsumo pi WHERE pi.id.idInsumo = :idInsumo")
    List<Integer> findProductosByInsumo(@Param("idInsumo") Integer idInsumo);
}