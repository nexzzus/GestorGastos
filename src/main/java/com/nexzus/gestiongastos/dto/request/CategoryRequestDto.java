package com.nexzus.gestiongastos.dto.request;

import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.enums.CategoryEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
public record CategoryRequestDto(
        @NotNull(message = "El nombre de la categoría es requerida")
        CategoryEnum name,

        @NotNull(message = "La descripción de la categoría es requerida")
        @Size(min = 5, max = 255, message = "La descripción de la categoría debe tener entre {min} y {max} caracteres")
        String description
) implements Serializable {
}