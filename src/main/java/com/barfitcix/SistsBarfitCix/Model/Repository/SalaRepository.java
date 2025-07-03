// src/main/java/com/barfitcix/SistsBarfitCix/Model/Repository/SalaRepository.java
package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
    // Buscar sala por nombre exacto
    Optional<Sala> findByNomSala(String nomSala);

    // Verificar si existe una sala con el mismo nombre
    boolean existsByNomSala(String nomSala);

    // Obtener todas las salas ordenadas alfabéticamente
    List<Sala> findAllByOrderByNomSalaAsc();
}