package com.barfitcix.SistsBarfitCix.Model.Repository;

import com.barfitcix.SistsBarfitCix.Model.entidad.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {

    // Buscar empresa por nombre
    Optional<Empresa> findByNomEmpresa(String nomEmpresa);

    // Verificar si existe una empresa con el mismo nombre
    boolean existsByNomEmpresa(String nomEmpresa);

    // Verificar si existe una empresa con el mismo nombre excluyendo un ID específico (para actualización)
    boolean existsByNomEmpresaAndIdInfoEmpresaNot(String nomEmpresa, Integer idInfoEmpresa);

    // Obtener empresas modificadas por un empleado específico
    List<Empresa> findByIdEmpleado(Integer idEmpleado);

    // Obtener empresa con información del empleado que la modificó
    @Query("SELECT e FROM Empresa e WHERE e.idInfoEmpresa = :idInfoEmpresa")
    Optional<Empresa> findByIdWithEmployee(@Param("idInfoEmpresa") Integer idInfoEmpresa);

    // Obtener todas las empresas ordenadas por fecha de modificación (más reciente primero)
    List<Empresa> findAllByOrderByFecModificacionInfoDesc();
}
