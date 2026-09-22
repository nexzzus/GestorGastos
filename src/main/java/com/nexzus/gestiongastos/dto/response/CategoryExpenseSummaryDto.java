package com.nexzus.gestiongastos.dto.response;

import java.math.BigDecimal;

public record CategoryExpenseSummaryDto(
        String categoryName,
        BigDecimal totalAmount
) {}