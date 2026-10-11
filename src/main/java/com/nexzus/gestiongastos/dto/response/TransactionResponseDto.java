package com.nexzus.gestiongastos.dto.response;

import com.nexzus.gestiongastos.model.enums.PaymentMethodEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponseDto(
        UUID id,
        String title,
        BigDecimal amount,
        LocalDateTime createdAt,
        String description,
        PaymentMethodEnum paymentMethod,
        CategoryResponseDto category,
        String type
) {
}
