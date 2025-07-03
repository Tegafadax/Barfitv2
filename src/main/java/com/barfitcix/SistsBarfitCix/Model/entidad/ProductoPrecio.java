package com.barfitcix.SistsBarfitCix.Model.entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productoPrecio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPrecio")
    private Integer idPrecio;

    @Column(name = "idProducto", nullable = false)
    private Integer idProducto;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "fechaInicio", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaInicio;

    @Column(name = "fechaFin")
    private LocalDateTime fechaFin;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "idEmpleadoModifico", nullable = false)
    private Integer idEmpleadoModifico;

    // Relaciones lazy para evitar N+1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProducto", insertable = false, updatable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleadoModifico", insertable = false, updatable = false)
    private Empleado empleadoModifico;
}