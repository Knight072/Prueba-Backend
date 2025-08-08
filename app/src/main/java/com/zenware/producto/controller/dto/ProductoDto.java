package com.zenware.producto.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductoDto(
        @NotBlank(message="El nombre es obligatorio") String nombre,
        @Min(value=0, message="El precio no puede ser negativo") double precio
) {}
