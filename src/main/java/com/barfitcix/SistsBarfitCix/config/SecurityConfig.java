package com.barfitcix.SistsBarfitCix.config;

import com.barfitcix.SistsBarfitCix.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (no requieren autenticación)
                        .requestMatchers("/auth/login").permitAll()

                        // Solo permitir crear empleados sin auth (para el primer admin)
                        .requestMatchers(HttpMethod.POST, "/empleados").permitAll()

                        // TODOS los demás endpoints de empleados requieren autenticación
                        .requestMatchers(HttpMethod.GET, "/empleados/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/empleados/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/empleados/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/empleados/**").authenticated()

                        // Endpoints de autenticación requieren estar logueado
                        .requestMatchers("/auth/logout", "/auth/verify", "/auth/me").authenticated()

                        // Endpoints de empresas requieren autenticación
                        .requestMatchers("/empresas/**").authenticated()
                        .requestMatchers("/empresa/**").authenticated() // Compatibilidad con endpoints antiguos

                        // Endpoints de informes requieren autenticación
                        .requestMatchers("/informes/**").authenticated()

                        // Endpoints de catálogos (solo lectura) requieren autenticación
                        .requestMatchers("/tipos-cantidad/**").authenticated()
                        .requestMatchers("/metodos-pago/**").authenticated()

                        // Endpoints de productos e insumos (CRUD completo) requieren autenticación
                        .requestMatchers("/productos/**").authenticated()
                        .requestMatchers("/insumos/**").authenticated()



                        // Cualquier otra request requiere autenticación
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}