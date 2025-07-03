package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar producto por nombre exacto
    Optional<Producto> findByNomProducto(String nomProducto);

    // Buscar producto por nombre ignorando mayúsculas/minúsculas
    Optional<Producto> findByNomProductoIgnoreCase(String nomProducto);

    // Verificar si existe un producto con el mismo nombre
    boolean existsByNomProducto(String nomProducto);

    // Verificar si existe un producto con el mismo nombre excluyendo un ID específico (para actualización)
    boolean existsByNomProductoAndIdProductoNot(String nomProducto, Integer idProducto);

    // Obtener todos los productos ordenados alfabéticamente
    List<Producto> findAllByOrderByNomProductoAsc();

    // Buscar productos que contengan texto en el nombre
    List<Producto> findByNomProductoContainingIgnoreCaseOrderByNomProductoAsc(String texto);
}