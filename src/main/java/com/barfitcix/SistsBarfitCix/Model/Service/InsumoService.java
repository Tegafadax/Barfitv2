package com.barfitcix.SistsBarfitCix.Model.Service;

import com.barfitcix.SistsBarfitCix.Model.DTO.InsumoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.entidad.Insumo;
import com.barfitcix.SistsBarfitCix.Model.Repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InsumoService {

    private final InsumoRepository insumoRepository;
    private final TipoCantidadService tipoCantidadService; // Para validaciones y enriquecimiento

    // Crear nuevo insumo
    public InsumoResponseDTO crearInsumo(InsumoRequestDTO requestDTO) {
        // Validar que el tipo de cantidad existe
        if (!tipoCantidadService.existePorId(requestDTO.getIdTipoCantidad())) {
            throw new RuntimeException("El tipo de cantidad con ID " + requestDTO.getIdTipoCantidad() + " no existe");
        }

        // Verificar que el nombre de insumo no exista
        if (insumoRepository.existsByNomInsumo(requestDTO.getNomInsumo())) {
            throw new RuntimeException("Ya existe un insumo con este nombre: " + requestDTO.getNomInsumo());
        }

        // Crear y configurar nuevo insumo
        Insumo insumo = new Insumo();
        insumo.setNomInsumo(requestDTO.getNomInsumo().trim());
        insumo.setIdTipoCantidad(requestDTO.getIdTipoCantidad());

        Insumo insumoGuardado = insumoRepository.save(insumo);
        return convertirAResponseDTO(insumoGuardado);
    }

    // Obtener insumo por ID
    @Transactional(readOnly = true)
    public Optional<InsumoResponseDTO> obtenerInsumoPorId(Integer id) {
        return insumoRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    // Obtener todos los insumos
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> obtenerTodosLosInsumos() {
        return insumoRepository.findAllByOrderByNomInsumoAsc()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar insumos por texto en el nombre
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> buscarInsumosPorNombre(String texto) {
        return insumoRepository.findByNomInsumoContainingIgnoreCaseOrderByNomInsumoAsc(texto)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener insumos por tipo de cantidad
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> obtenerInsumosPorTipoCantidad(Integer idTipoCantidad) {
        // Validar que el tipo de cantidad existe
        if (!tipoCantidadService.existePorId(idTipoCantidad)) {
            throw new RuntimeException("El tipo de cantidad con ID " + idTipoCantidad + " no existe");
        }

        return insumoRepository.findByIdTipoCantidadOrderByNomInsumoAsc(idTipoCantidad)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener insumo por nombre exacto
    @Transactional(readOnly = true)
    public Optional<InsumoResponseDTO> obtenerInsumoPorNombre(String nomInsumo) {
        return insumoRepository.findByNomInsumoIgnoreCase(nomInsumo)
                .map(this::convertirAResponseDTO);
    }

    // Actualizar insumo
    public InsumoResponseDTO actualizarInsumo(Integer id, InsumoUpdateDTO updateDTO) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));

        // Validar tipo de cantidad si se está actualizando
        if (updateDTO.getIdTipoCantidad() != null) {
            if (!tipoCantidadService.existePorId(updateDTO.getIdTipoCantidad())) {
                throw new RuntimeException("El tipo de cantidad con ID " + updateDTO.getIdTipoCantidad() + " no existe");
            }
            insumo.setIdTipoCantidad(updateDTO.getIdTipoCantidad());
        }

        // Verificar nombre único si se está actualizando
        if (updateDTO.getNomInsumo() != null &&
                !updateDTO.getNomInsumo().trim().equals(insumo.getNomInsumo())) {
            if (insumoRepository.existsByNomInsumoAndIdInsumoNot(
                    updateDTO.getNomInsumo().trim(), id)) {
                throw new RuntimeException("Ya existe otro insumo con este nombre: " + updateDTO.getNomInsumo());
            }
            insumo.setNomInsumo(updateDTO.getNomInsumo().trim());
        }

        Insumo insumoActualizado = insumoRepository.save(insumo);
        return convertirAResponseDTO(insumoActualizado);
    }

    // Eliminar insumo
    public boolean eliminarInsumo(Integer id) {
        if (!insumoRepository.existsById(id)) {
            throw new RuntimeException("Insumo no encontrado con ID: " + id);
        }

        // TODO: Verificar dependencias antes de eliminar
        // - ProductoInsumo
        // Por ahora permitimos eliminar directamente

        insumoRepository.deleteById(id);
        return true;
    }

    // Verificar si existe un insumo por ID (para validaciones en otras tablas)
    @Transactional(readOnly = true)
    public boolean existeInsumoPorId(Integer id) {
        return insumoRepository.existsById(id);
    }

    // Verificar si existe un insumo por nombre
    @Transactional(readOnly = true)
    public boolean existeInsumoPorNombre(String nomInsumo) {
        return insumoRepository.existsByNomInsumo(nomInsumo);
    }

    // Obtener nombre de insumo por ID (para enriquecer respuestas de otras tablas)
    @Transactional(readOnly = true)
    public String obtenerNombreInsumoPorId(Integer id) {
        return insumoRepository.findById(id)
                .map(Insumo::getNomInsumo)
                .orElse("Insumo no encontrado");
    }

    // Contar insumos por tipo de cantidad
    @Transactional(readOnly = true)
    public long contarInsumosPorTipoCantidad(Integer idTipoCantidad) {
        return insumoRepository.countByIdTipoCantidad(idTipoCantidad);
    }

    // Método auxiliar para convertir Entity a ResponseDTO (enriquecido)
    private InsumoResponseDTO convertirAResponseDTO(Insumo insumo) {
        InsumoResponseDTO responseDTO = new InsumoResponseDTO();
        responseDTO.setIdInsumo(insumo.getIdInsumo());
        responseDTO.setNomInsumo(insumo.getNomInsumo());
        responseDTO.setIdTipoCantidad(insumo.getIdTipoCantidad());

        // Enriquecer con el nombre del tipo de cantidad
        responseDTO.setNomTipoCantidad(
                tipoCantidadService.obtenerNombrePorId(insumo.getIdTipoCantidad())
        );

        return responseDTO;
    }
}