package com.barfitcix.SistsBarfitCix.security;

import com.barfitcix.SistsBarfitCix.Model.entidad.Empleado;
import com.barfitcix.SistsBarfitCix.Model.Repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emaCorporativo) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByEmaCorporativo(emaCorporativo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + emaCorporativo));

        // Verificar que el empleado esté activo
        if (!empleado.getActivo()) {
            throw new UsernameNotFoundException(
                    "El empleado está desactivado: " + emaCorporativo);
        }

        return User.builder()
                .username(empleado.getEmaCorporativo())
                .password(empleado.getContrasena())
                .authorities(getAuthorities(empleado))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!empleado.getActivo())
                .build();
    }

    // Asignar autoridades basadas en el rol
    private Collection<? extends GrantedAuthority> getAuthorities(Empleado empleado) {
        String rol = empleado.getIdRol() == 1 ? "ROLE_ADMIN" : "ROLE_EMPLEADO";
        return Collections.singletonList(new SimpleGrantedAuthority(rol));
    }

    // Método adicional para obtener empleado por email (usado en el servicio de auth)
    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoPorEmail(String emaCorporativo) {
        return empleadoRepository.findByEmaCorporativo(emaCorporativo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + emaCorporativo));
    }
}