// src/main/java/com/barfitcix/SistsBarfitCix/Model/Repository/MesaRepository.java
package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Integer> {
    // Buscar mesa por código exacto (dentro de una sala, si fuera necesario)
    Optional<Mesa> findByCodMesa(String codMesa);

    // Verificar si existe una mesa con el mismo código en una sala específica
    boolean existsByCodMesaAndSala_IdSala(String codMesa, Integer idSala);

    // Obtener mesas por sala
    List<Mesa> findBySala_IdSalaOrderByCodMesaAsc(Integer idSala);

    // Obtener mesas por estado
    List<Mesa> findByEstadoOrderBySala_NomSalaAscCodMesaAsc(String estado);

    // Obtener todas las mesas con la Sala asociada
    // (Fetch Join para evitar N+1 si se necesita el nombre de la sala al listar mesas)
    // @Query("SELECT m FROM Mesa m JOIN FETCH m.sala ORDER BY m.sala.nomSala ASC, m.codMesa ASC")
    List<Mesa> findAllByOrderBySala_NomSalaAscCodMesaAsc();

    // Actualizar el estado de una mesa
    // @Modifying
    // @Query("UPDATE Mesa m SET m.estado = :nuevoEstado WHERE m.idMesa = :idMesa")
    // int updateEstadoMesa(@Param("idMesa") Integer idMesa, @Param("nuevoEstado") String nuevoEstado);
}