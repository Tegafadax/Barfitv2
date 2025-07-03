package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {

    // Obtener todos ordenados alfabéticamente por nombre
    List<MetodoPago> findAllByOrderByNomMetodoAsc();

    // Buscar por nombre exacto
    Optional<MetodoPago> findByNomMetodo(String nomMetodo);

    // Buscar por nombre ignorando mayúsculas/minúsculas
    Optional<MetodoPago> findByNomMetodoIgnoreCase(String nomMetodo);

    // Verificar si existe por nombre
    boolean existsByNomMetodo(String nomMetodo);

    // Buscar que contengan texto (para búsquedas)
    List<MetodoPago> findByNomMetodoContainingIgnoreCaseOrderByNomMetodoAsc(String texto);
}