package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Integer> {

    // Buscar insumo por nombre exacto
    Optional<Insumo> findByNomInsumo(String nomInsumo);

    // Buscar insumo por nombre ignorando mayúsculas/minúsculas
    Optional<Insumo> findByNomInsumoIgnoreCase(String nomInsumo);

    // Verificar si existe un insumo con el mismo nombre
    boolean existsByNomInsumo(String nomInsumo);

    // Verificar si existe un insumo con el mismo nombre excluyendo un ID específico (para actualización)
    boolean existsByNomInsumoAndIdInsumoNot(String nomInsumo, Integer idInsumo);

    // Obtener todos los insumos ordenados alfabéticamente
    List<Insumo> findAllByOrderByNomInsumoAsc();

    // Buscar insumos que contengan texto en el nombre
    List<Insumo> findByNomInsumoContainingIgnoreCaseOrderByNomInsumoAsc(String texto);

    // Obtener insumos por tipo de cantidad
    List<Insumo> findByIdTipoCantidadOrderByNomInsumoAsc(Integer idTipoCantidad);

    // Verificar si existen insumos con un tipo de cantidad específico (para validaciones)
    boolean existsByIdTipoCantidad(Integer idTipoCantidad);

    // Contar insumos por tipo de cantidad
    long countByIdTipoCantidad(Integer idTipoCantidad);
}