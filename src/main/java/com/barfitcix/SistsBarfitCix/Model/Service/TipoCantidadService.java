package com.barfitcix.SistsBarfitCix.Model.Service;


import com.barfitcix.SistsBarfitCix.Model.DTO.TipoCantidadDTO.TipoCantidadResponseDTO;
import com.barfitcix.SistsBarfitCix.Model.entidad.TipoCantidad;
import com.barfitcix.SistsBarfitCix.Model.Repository.TipoCantidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Solo operaciones de lectura
public class TipoCantidadService {

    private final TipoCantidadRepository tipoCantidadRepository;

    // Obtener todos los tipos de cantidad ordenados alfabéticamente
    public List<TipoCantidadResponseDTO> obtenerTodos() {
        return tipoCantidadRepository.findAllByOrderByNomCantidadAsc()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener tipo de cantidad por ID
    public Optional<TipoCantidadResponseDTO> obtenerPorId(Integer id) {
        return tipoCantidadRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    // Obtener tipo de cantidad por nombre exacto
    public Optional<TipoCantidadResponseDTO> obtenerPorNombre(String nomCantidad) {
        return tipoCantidadRepository.findByNomCantidadIgnoreCase(nomCantidad)
                .map(this::convertirAResponseDTO);
    }

    // Buscar tipos de cantidad que contengan texto
    public List<TipoCantidadResponseDTO> buscarPorTexto(String texto) {
        return tipoCantidadRepository.findByNomCantidadContainingIgnoreCaseOrderByNomCantidadAsc(texto)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Verificar si existe un tipo de cantidad por ID (para validaciones en otras tablas)
    public boolean existePorId(Integer id) {
        return tipoCantidadRepository.existsById(id);
    }

    // Verificar si existe un tipo de cantidad por nombre
    public boolean existePorNombre(String nomCantidad) {
        return tipoCantidadRepository.existsByNomCantidad(nomCantidad);
    }

    // Obtener nombre de tipo de cantidad por ID (para enriquecer respuestas de otras tablas)
    public String obtenerNombrePorId(Integer id) {
        return tipoCantidadRepository.findById(id)
                .map(TipoCantidad::getNomCantidad)
                .orElse("Tipo no encontrado");
    }

    // Método auxiliar para convertir Entity a ResponseDTO
    private TipoCantidadResponseDTO convertirAResponseDTO(TipoCantidad tipoCantidad) {
        return new TipoCantidadResponseDTO(
                tipoCantidad.getIdTipoCantidad(),
                tipoCantidad.getNomCantidad()
        );
    }
}
