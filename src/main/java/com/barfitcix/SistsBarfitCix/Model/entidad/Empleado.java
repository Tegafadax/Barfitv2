package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmpleado")
    private Integer idEmpleado;

    @Column(name = "nomEmpleado", nullable = false, length = 100)
    private String nomEmpleado;

    @Column(name = "emaCorporativo", nullable = false, unique = true, length = 255)
    private String emaCorporativo;

    @Column(name = "fecIngreso", nullable = false, updatable = false)
    private LocalDateTime fecIngreso;

    @Column(name = "fecSalida")
    private LocalDateTime fecSalida;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "idRol", nullable = false)
    private Integer idRol;

    @PrePersist
    protected void onCreate() {
        if (fecIngreso == null) {
            fecIngreso = LocalDateTime.now();
        }
        if (activo == null) {
            activo = true;
        }
    }
}