package com.nexzus.gestiongastos.model;

import com.nexzus.gestiongastos.model.enums.CategoryEnum;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link Category}
 */
public record CategoryResponseDto(
        UUID id,
        CategoryEnum name) implements Serializable {
}