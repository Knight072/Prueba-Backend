package com.zenware.producto.controller;

import com.zenware.producto.controller.dto.ProductoDto;
import com.zenware.producto.model.Producto;
import com.zenware.producto.repository.ProductoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoController {

    @Inject
    ProductoRepository repository;

    @GET
    public List<Producto> all() {
        return repository.listAll();
    }

    @GET
    @Path("{id}")
    public Producto byId(@PathParam("id") Long id) {
        Producto p = repository.findById(id);
        if (p == null) {
            throw new NotFoundException("No encontrado");
        }
        return p;
    }

    @POST
    @Transactional
    public Response create(@Valid ProductoDto request) {
        Producto p = new Producto(request.nombre(), request.precio());
        repository.persist(p);
        return Response.created(URI.create("/productos/" + p.getId())).entity(p).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("No encontrado");
        }
        return Response.noContent().build();
    }
}


