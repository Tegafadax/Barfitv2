// src/main/java/com/barfitcix/SistsBarfitCix/Model/Repository/PedidoMesaRepository.java
package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.PedidoMesa;
import com.barfitcix.SistsBarfitCix.Model.entidad.PedidoMesaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoMesaRepository extends JpaRepository<PedidoMesa, PedidoMesaPK> {

    // Obtener todas las asociaciones de mesa para un pedido específico
    @Query("SELECT pm FROM PedidoMesa pm JOIN FETCH pm.mesa WHERE pm.id.idPedido = :idPedido")
    List<PedidoMesa> findByPedidoIdWithMesaDetails(@Param("idPedido") Integer idPedido);

    // Verificar si una mesa está asociada a algún pedido (útil para la unicidad y eliminar mesa)
    boolean existsById_IdMesa(Integer idMesa);

    // Eliminar todas las asociaciones de mesa para un pedido específico
    // (Aunque el trigger maneja la limpieza al finalizar el pedido, esto es útil si se necesita manualmente)
    @Modifying
    @Query("DELETE FROM PedidoMesa pm WHERE pm.id.idPedido = :idPedido")
    void deleteByIdPedido(@Param("idPedido") Integer idPedido);

    // Encontrar un registro PedidoMesa por ID de Mesa
    Optional<PedidoMesa> findById_IdMesa(Integer idMesa);
}