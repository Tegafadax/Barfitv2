package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.Informe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InformeRepository extends JpaRepository<Informe, Integer> {

    // Buscar informe por nombre
    Optional<Informe> findByNomInforme(String nomInforme);

    // Verificar si existe un informe con el mismo nombre
    boolean existsByNomInforme(String nomInforme);

    // Verificar si existe un informe con el mismo nombre excluyendo un ID específico (para actualización)
    boolean existsByNomInformeAndIdInformeNot(String nomInforme, Integer idInforme);

    // Obtener informes generados por un empleado específico
    List<Informe> findByIdEmpleado(Integer idEmpleado);

    // Obtener todos los informes ordenados por fecha (más reciente primero)
    List<Informe> findAllByOrderByFecInformeDesc();

    // Buscar informes por rango de fechas
    @Query("SELECT i FROM Informe i WHERE i.fecInforme BETWEEN :fechaInicio AND :fechaFin ORDER BY i.fecInforme DESC")
    List<Informe> findByFecInformeBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    // Buscar informes por año específico
    @Query("SELECT i FROM Informe i WHERE YEAR(i.fecInforme) = :año ORDER BY i.fecInforme DESC")
    List<Informe> findByAño(@Param("año") Integer año);

    // Buscar informes por mes y año específico
    @Query("SELECT i FROM Informe i WHERE YEAR(i.fecInforme) = :año AND MONTH(i.fecInforme) = :mes ORDER BY i.fecInforme DESC")
    List<Informe> findByMesYAño(@Param("mes") Integer mes, @Param("año") Integer año);

    // Obtener informes generados por un empleado en un rango de fechas
    @Query("SELECT i FROM Informe i WHERE i.idEmpleado = :idEmpleado AND i.fecInforme BETWEEN :fechaInicio AND :fechaFin ORDER BY i.fecInforme DESC")
    List<Informe> findByEmpleadoAndFechas(@Param("idEmpleado") Integer idEmpleado,
                                          @Param("fechaInicio") LocalDateTime fechaInicio,
                                          @Param("fechaFin") LocalDateTime fechaFin);

    // Buscar informes que contengan texto en el nombre
    List<Informe> findByNomInformeContainingIgnoreCaseOrderByFecInformeDesc(String texto);
}
