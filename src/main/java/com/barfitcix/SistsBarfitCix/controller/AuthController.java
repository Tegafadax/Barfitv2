package com.barfitcix.SistsBarfitCix.controller;

import com.barfitcix.SistsBarfitCix.Model.DTO.LoginDTO.*;
import com.barfitcix.SistsBarfitCix.Model.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO loginResponse = authService.login(loginRequest);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login exitoso",
                    "data", loginResponse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        try {
            String token = getJwtFromRequest(request);
            LogoutResponseDTO logoutResponse = authService.logout(token);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Logout exitoso",
                    "data", logoutResponse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Verificar estado del token
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyToken(HttpServletRequest request) {
        try {
            String token = getJwtFromRequest(request);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Token no proporcionado"
                ));
            }

            if (authService.isTokenBlacklisted(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "success", false,
                        "message", "Token invalidado"
                ));
            }

            // Obtener información del empleado autenticado
            var empleado = authService.obtenerEmpleadoAutenticado();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Token válido",
                    "data", Map.of(
                            "idEmpleado", empleado.getIdEmpleado(),
                            "nomEmpleado", empleado.getNomEmpleado(),
                            "emaCorporativo", empleado.getEmaCorporativo(),
                            "idRol", empleado.getIdRol(),
                            "nombreRol", empleado.getIdRol() == 1 ? "ADMIN" : "EMPLEADO"
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Token inválido: " + e.getMessage()
            ));
        }
    }

    // Obtener información del usuario actual
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        try {
            var empleado = authService.obtenerEmpleadoAutenticado();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Información del usuario actual",
                    "data", Map.of(
                            "idEmpleado", empleado.getIdEmpleado(),
                            "nomEmpleado", empleado.getNomEmpleado(),
                            "emaCorporativo", empleado.getEmaCorporativo(),
                            "idRol", empleado.getIdRol(),
                            "nombreRol", empleado.getIdRol() == 1 ? "ADMIN" : "EMPLEADO",
                            "activo", empleado.getActivo()
                    )
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Usuario no autenticado: " + e.getMessage()
            ));
        }
    }

    // Extraer JWT del header Authorization
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}