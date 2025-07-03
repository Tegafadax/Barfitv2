package com.barfitcix.SistsBarfitCix.Model.Repository;
import com.barfitcix.SistsBarfitCix.Model.entidad.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    // Buscar empleado por email corporativo
    Optional<Empleado> findByEmaCorporativo(String emaCorporativo);

    // Verificar si existe un email corporativo (para validación)
    boolean existsByEmaCorporativo(String emaCorporativo);

    // Verificar si existe un email corporativo excluyendo un ID específico (para actualización)
    boolean existsByEmaCorporativoAndIdEmpleadoNot(String emaCorporativo, Integer idEmpleado);

    // Obtener todos los empleados activos
    List<Empleado> findByActivoTrue();

    // Obtener todos los empleados inactivos
    List<Empleado> findByActivoFalse();

    // Obtener empleados por rol
    List<Empleado> findByIdRol(Integer idRol);

    // Obtener empleados activos por rol
    List<Empleado> findByIdRolAndActivoTrue(Integer idRol);

    // Desactivar empleado (eliminación lógica)
    @Modifying
    @Transactional
    @Query("UPDATE Empleado e SET e.activo = false, e.fecSalida = :fechaSalida WHERE e.idEmpleado = :idEmpleado")
    int desactivarEmpleado(@Param("idEmpleado") Integer idEmpleado, @Param("fechaSalida") LocalDateTime fechaSalida);

    // Reactivar empleado
    @Modifying
    @Transactional
    @Query("UPDATE Empleado e SET e.activo = true, e.fecSalida = null WHERE e.idEmpleado = :idEmpleado")
    int reactivarEmpleado(@Param("idEmpleado") Integer idEmpleado);
}