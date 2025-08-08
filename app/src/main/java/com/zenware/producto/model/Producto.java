package com.zenware.producto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "productos")
public class Producto extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Min(value = 0, message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private double precio;

    public Producto() {}

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public void aplicarImpuesto(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 50) {
            throw new IllegalArgumentException("Porcentaje fuera de rango (0-50)");
        }
        double incremento = precio * (porcentaje / 100.0);
        this.precio = precio + incremento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}

