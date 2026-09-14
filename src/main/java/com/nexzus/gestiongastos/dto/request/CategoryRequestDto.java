package com.nexzus.gestiongastos.dto.request;

import com.nexzus.gestiongastos.model.Category;
import com.nexzus.gestiongastos.model.enums.CategoryEnum;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
public record CategoryRequestDto(
        @NotNull(message = "El nombre de la categoría es requerida")
        CategoryEnum name) implements Serializable {
}