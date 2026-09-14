package com.nexzus.gestiongastos.dto.response;

import com.nexzus.gestiongastos.model.Category;
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