package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "informe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInforme")
    private Integer idInforme;

    @Column(name = "fecInforme", nullable = false)
    private LocalDateTime fecInforme;

    @Column(name = "linkInforme", nullable = false, length = 255)
    private String linkInforme;

    @Column(name = "nomInforme", nullable = false, length = 255)
    private String nomInforme;

    @Column(name = "idEmpleado", nullable = false)
    private Integer idEmpleado; // FK al empleado que generó el informe

    @PrePersist
    protected void onCreate() {
        if (fecInforme == null) {
            fecInforme = LocalDateTime.now();
        }
    }
}
