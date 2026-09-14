package com.nexzus.gestiongastos.model;

import com.nexzus.gestiongastos.model.enums.CategoryEnum;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
public record CategoryResponseDto(CategoryEnum name) implements Serializable {
}