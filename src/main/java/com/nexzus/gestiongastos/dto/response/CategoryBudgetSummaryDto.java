package com.nexzus.gestiongastos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryBudgetSummaryDto(
        UUID categoryId,
        String categoryName,
        BigDecimal budgetAmount,
        BigDecimal spentAmount,
        BigDecimal remainingAmount,
        BigDecimal usedPercentage,
        LocalDateTime endDate
) {}

