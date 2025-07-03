package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.TipoCantidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoCantidadRepository extends JpaRepository<TipoCantidad, Integer> {

    // Obtener todos ordenados alfabéticamente por nombre
    List<TipoCantidad> findAllByOrderByNomCantidadAsc();

    // Buscar por nombre exacto
    Optional<TipoCantidad> findByNomCantidad(String nomCantidad);

    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<TipoCantidad> findByNomCantidadIgnoreCase(String nomCantidad);

    // Verificar si existe por nombre
    boolean existsByNomCantidad(String nomCantidad);

    // Buscar que contengan texto (para búsquedas)
    List<TipoCantidad> findByNomCantidadContainingIgnoreCaseOrderByNomCantidadAsc(String texto);

    boolean existsByIdTipoCantidad(Integer idTipoCantidad);
}
