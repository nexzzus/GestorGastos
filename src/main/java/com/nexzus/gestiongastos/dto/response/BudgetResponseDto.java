package com.nexzus.gestiongastos.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link com.nexzus.gestiongastos.model.Budget}
 */
public record BudgetResponseDto(
        UUID id,
        BigDecimal amount,
        LocalDate startDate,
        LocalDate endDate,
        CategoryResponseDto category
) implements Serializable {
}