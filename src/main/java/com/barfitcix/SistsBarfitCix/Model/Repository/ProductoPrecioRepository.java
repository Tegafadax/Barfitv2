package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.ProductoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoPrecioRepository extends JpaRepository<ProductoPrecio, Integer> {

    // Obtener precio activo de un producto
    @Query("SELECT pp FROM ProductoPrecio pp WHERE pp.idProducto = :idProducto AND pp.activo = true AND pp.fechaFin IS NULL")
    Optional<ProductoPrecio> findPrecioActivoByProducto(@Param("idProducto") Integer idProducto);

    // Obtener historial de precios de un producto
    @Query("SELECT pp FROM ProductoPrecio pp WHERE pp.idProducto = :idProducto ORDER BY pp.fechaInicio DESC")
    List<ProductoPrecio> findHistorialByProducto(@Param("idProducto") Integer idProducto);

    // Cerrar precio anterior (marcar fechaFin y activo = false)
    @Modifying
    @Query("UPDATE ProductoPrecio pp SET pp.fechaFin = :fechaFin, pp.activo = false WHERE pp.idProducto = :idProducto AND pp.activo = true")
    void cerrarPrecioAnterior(@Param("idProducto") Integer idProducto, @Param("fechaFin") LocalDateTime fechaFin);

    // Verificar si producto tiene precio activo
    boolean existsByIdProductoAndActivoTrueAndFechaFinIsNull(Integer idProducto);

    // Obtener todos los precios activos
    List<ProductoPrecio> findByActivoTrueAndFechaFinIsNull();

    // En ProductoPrecioRepository.java
    @Modifying
    @Query("DELETE FROM ProductoPrecio pp WHERE pp.idProducto = :idProducto")
    void deleteByIdProducto(@Param("idProducto") Integer idProducto);
}