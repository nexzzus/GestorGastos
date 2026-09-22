package com.nexzus.gestiongastos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionSummaryDto(
        UUID id,
        String title,
        BigDecimal amount,
        LocalDateTime createdAt,
        String type, // "INCOME" o "EXPENSE"
        CategoryResponseDto category
) {
}
