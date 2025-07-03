package com.barfitcix.SistsBarfitCix.security;

import com.barfitcix.SistsBarfitCix.Model.Service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtUtil.validarToken(jwt)) {
                // Verificar si el token está en la blacklist a través del servicio
                // Nota: Para evitar dependencia circular, verificaremos directamente aquí
                // En una implementación real, podrías usar Redis o una base de datos

                String emaCorporativo = jwtUtil.getEmailFromToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(emaCorporativo);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Agregar información del empleado al request para fácil acceso
                    request.setAttribute("idEmpleado", jwtUtil.getIdEmpleadoFromToken(jwt));
                    request.setAttribute("idRol", jwtUtil.getIdRolFromToken(jwt));
                    request.setAttribute("nomEmpleado", jwtUtil.getNombreFromToken(jwt));
                    request.setAttribute("jwt", jwt); // Para verificaciones posteriores
                }
            }
        } catch (Exception ex) {
            log.error("No se pudo establecer autenticación de usuario en el contexto de seguridad", ex);
        }

        filterChain.doFilter(request, response);
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