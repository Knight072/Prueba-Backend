package com.zenware.producto.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    @Test
    void aplicaImpuesto_ok() {
        var p = new Producto("Escoba", 100.0);
        p.aplicarImpuesto(10);
        assertEquals(110.0, p.getPrecio(), 1e-6);
    }

    @Test
    void aplicaImpuesto_fueraDeRango_lanza() {
        var p = new Producto("Jabón", 100.0);
        assertThrows(IllegalArgumentException.class, () -> p.aplicarImpuesto(60));
    }
}

