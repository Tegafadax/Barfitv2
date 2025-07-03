package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInfoEmpresa")
    private Integer idInfoEmpresa;

    @Column(name = "nomEmpresa", nullable = false, length = 255)
    private String nomEmpresa;

    @Column(name = "dirEmpresa", nullable = false, length = 255)
    private String dirEmpresa;

    @Column(name = "telEmpresa", nullable = false, length = 50)
    private String telEmpresa;

    @Column(name = "corEmpresa", nullable = false, length = 100)
    private String corEmpresa;

    @Column(name = "logoEmpresa", nullable = false, length = 255)
    private String logoEmpresa;

    @Column(name = "fecModificacionInfo", nullable = false)
    private LocalDateTime fecModificacionInfo;

    @Column(name = "idEmpleado", nullable = false)
    private Integer idEmpleado; // FK al empleado que modificó

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        fecModificacionInfo = LocalDateTime.now();
    }
}