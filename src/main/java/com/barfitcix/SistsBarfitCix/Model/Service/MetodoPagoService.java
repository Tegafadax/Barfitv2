package com.barfitcix.SistsBarfitCix.Model.Service;

import com.barfitcix.SistsBarfitCix.Model.DTO.MetodoPagoDTO.MetodoPagoResponseDTO;
import com.barfitcix.SistsBarfitCix.Model.entidad.MetodoPago;
import com.barfitcix.SistsBarfitCix.Model.Repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Solo operaciones de lectura
public class MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    // Obtener todos los métodos de pago ordenados alfabéticamente
    public List<MetodoPagoResponseDTO> obtenerTodos() {
        return metodoPagoRepository.findAllByOrderByNomMetodoAsc()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener método de pago por ID
    public Optional<MetodoPagoResponseDTO> obtenerPorId(Integer id) {
        return metodoPagoRepository.findById(id)
                .map(this::convertirAResponseDTO);
    }

    // Obtener método de pago por nombre exacto
    public Optional<MetodoPagoResponseDTO> obtenerPorNombre(String nomMetodo) {
        return metodoPagoRepository.findByNomMetodoIgnoreCase(nomMetodo)
                .map(this::convertirAResponseDTO);
    }

    // Buscar métodos de pago que contengan texto
    public List<MetodoPagoResponseDTO> buscarPorTexto(String texto) {
        return metodoPagoRepository.findByNomMetodoContainingIgnoreCaseOrderByNomMetodoAsc(texto)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Verificar si existe un método de pago por ID (para validaciones en otras tablas)
    public boolean existePorId(Integer id) {
        return metodoPagoRepository.existsById(id);
    }

    // Verificar si existe un método de pago por nombre
    public boolean existePorNombre(String nomMetodo) {
        return metodoPagoRepository.existsByNomMetodo(nomMetodo);
    }

    // Obtener nombre de método de pago por ID (para enriquecer respuestas de otras tablas)
    public String obtenerNombrePorId(Integer id) {
        return metodoPagoRepository.findById(id)
                .map(MetodoPago::getNomMetodo)
                .orElse("Método no encontrado");
    }

    // Método auxiliar para convertir Entity a ResponseDTO
    private MetodoPagoResponseDTO convertirAResponseDTO(MetodoPago metodoPago) {
        return new MetodoPagoResponseDTO(
                metodoPago.getIdMetodoPago(),
                metodoPago.getNomMetodo()
        );
    }
}