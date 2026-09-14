package com.nexzus.gestiongastos.dto.response;

import com.nexzus.gestiongastos.model.CategoryResponseDto;
import com.nexzus.gestiongastos.model.enums.PaymentMethodEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.nexzus.gestiongastos.model.Expense}
 */
public record ExpenseResponseDto(
        UUID id,
        String title,
        BigDecimal amount,
        LocalDateTime createdAt,
        String description,
        PaymentMethodEnum paymentMethod,
        CategoryResponseDto category
) implements Serializable {
}