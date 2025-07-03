package com.barfitcix.SistsBarfitCix.Model.Service;

import com.barfitcix.SistsBarfitCix.Model.DTO.EmpleadoDTO.*;
import com.barfitcix.SistsBarfitCix.Model.DTO.LoginDTO.*;
import com.barfitcix.SistsBarfitCix.Model.entidad.Empleado;
import com.barfitcix.SistsBarfitCix.security.CustomUserDetailsService;
import com.barfitcix.SistsBarfitCix.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    // Set para almacenar tokens invalidados (blacklist)
    private final Set<String> tokenBlacklist = new HashSet<>();

    // Login
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmaCorporativo(),
                            loginRequest.getContrasena()
                    )
            );

            // Establecer autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obtener datos del empleado
            Empleado empleado = userDetailsService.obtenerEmpleadoPorEmail(loginRequest.getEmaCorporativo());

            // Generar token JWT
            String token = jwtUtil.generarToken(
                    empleado.getEmaCorporativo(),
                    empleado.getIdEmpleado(),
                    empleado.getIdRol(),
                    empleado.getNomEmpleado()
            );

            // Crear respuesta
            EmpleadoResponseDTO empleadoResponse = convertirAResponseDTO(empleado);

            return new LoginResponseDTO(
                    token,
                    "Bearer",
                    jwtUtil.getExpirationAsLocalDateTime(token),
                    empleadoResponse
            );

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Email o contraseña incorrectos");
        } catch (Exception e) {
            throw new RuntimeException("Error durante el login: " + e.getMessage());
        }
    }

    // Logout
    public LogoutResponseDTO logout(String token) {
        try {
            // Agregar token a la blacklist
            if (token != null && jwtUtil.validarToken(token)) {
                tokenBlacklist.add(token);
            }

            // Limpiar contexto de seguridad
            SecurityContextHolder.clearContext();

            return new LogoutResponseDTO(
                    "Logout exitoso",
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error durante el logout: " + e.getMessage());
        }
    }

    // Verificar si el token está en la blacklist
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    // Obtener empleado autenticado actual
    public Empleado obtenerEmpleadoAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userDetailsService.obtenerEmpleadoPorEmail(email);
        }
        throw new RuntimeException("No hay empleado autenticado");
    }

    // Método auxiliar para convertir Entity a ResponseDTO
    private EmpleadoResponseDTO convertirAResponseDTO(Empleado empleado) {
        EmpleadoResponseDTO responseDTO = new EmpleadoResponseDTO();
        responseDTO.setIdEmpleado(empleado.getIdEmpleado());
        responseDTO.setNomEmpleado(empleado.getNomEmpleado());
        responseDTO.setEmaCorporativo(empleado.getEmaCorporativo());
        responseDTO.setFecIngreso(empleado.getFecIngreso());
        responseDTO.setFecSalida(empleado.getFecSalida());
        responseDTO.setActivo(empleado.getActivo());
        responseDTO.setIdRol(empleado.getIdRol());
        responseDTO.setNombreRol(empleado.getIdRol() == 1 ? "ADMIN" : "EMPLEADO");
        return responseDTO;
    }
}