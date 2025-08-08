package com.zenware.producto.repository;

import com.zenware.producto.model.Producto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductoRepository implements PanacheRepository<Producto> {

}

