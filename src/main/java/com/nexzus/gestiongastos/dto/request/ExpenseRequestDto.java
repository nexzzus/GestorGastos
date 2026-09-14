package com.nexzus.gestiongastos.dto.request;

import com.nexzus.gestiongastos.model.enums.PaymentMethodEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.nexzus.gestiongastos.model.Expense}
 */
public record ExpenseRequestDto(
        @NotNull(message = "El título es requerido")
        @Size(message = "El título debe tener entre {min} y {max} carácteres", min = 3, max = 64)
        String title,

        @NotNull(message = "El monto es requerido")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal amount,

        LocalDateTime createdAt,

        UUID categoryId,
        UUID userId,
        String description,
        PaymentMethodEnum paymentMethod) implements Serializable {
}