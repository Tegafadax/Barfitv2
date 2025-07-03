package com.barfitcix.SistsBarfitCix.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    // Generar token JWT
    public String generarToken(String emaCorporativo, Integer idEmpleado, Integer idRol, String nomEmpleado) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idEmpleado", idEmpleado);
        claims.put("idRol", idRol);
        claims.put("nomEmpleado", nomEmpleado);
        claims.put("rol", idRol == 1 ? "ADMIN" : "EMPLEADO");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .claims(claims)
                .subject(emaCorporativo)
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Obtener email del token
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Obtener ID del empleado del token
    public Integer getIdEmpleadoFromToken(String token) {
        return getClaimsFromToken(token).get("idEmpleado", Integer.class);
    }

    // Obtener rol del empleado del token
    public Integer getIdRolFromToken(String token) {
        return getClaimsFromToken(token).get("idRol", Integer.class);
    }

    // Obtener nombre del empleado del token
    public String getNombreFromToken(String token) {
        return getClaimsFromToken(token).get("nomEmpleado", String.class);
    }

    // Obtener fecha de expiración
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    // Obtener fecha de expiración como LocalDateTime
    public LocalDateTime getExpirationAsLocalDateTime(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // Validar token
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Token JWT con firma inválida: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Token JWT malformado: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no soportado: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string está vacío: {}", ex.getMessage());
        }
        return false;
    }

    // Verificar si el token ha expirado
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Obtener claims del token
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Obtener la clave de firma
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}