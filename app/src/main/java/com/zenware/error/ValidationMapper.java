package com.zenware.error;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .findFirst().map(v -> v.getMessage()).orElse("Solicitud inválida");
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiError(400, msg))
                .build();
    }
}

