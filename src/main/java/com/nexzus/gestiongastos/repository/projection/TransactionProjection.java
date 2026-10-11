package com.nexzus.gestiongastos.repository.projection;

import com.nexzus.gestiongastos.model.enums.PaymentMethodEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface TransactionProjection {
    UUID getId();

    String getTitle();

    BigDecimal getAmount();

    LocalDateTime getCreatedAt();

    String getDescription();

    PaymentMethodEnum getPaymentMethod();

    UUID getCategoryId();

    String getCategoryName();
    String getCategoryDescription();

    String getType();
}
