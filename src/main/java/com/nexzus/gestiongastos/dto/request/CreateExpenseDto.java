package com.nexzus.gestiongastos.dto.request;

import com.nexzus.gestiongastos.model.enums.PaymentMethodEnum;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.nexzus.gestiongastos.model.Expense}
 */
public record CreateExpenseDto(
        @NotNull(message = "El título es requerido")
        @Size(message = "El título debe tener entre {min} y {max} carácteres", min = 3, max = 64)
        String title,
        @NotNull(message = "El monto es requerido")
        @Positive(message = "El monto debe ser mayor a cero")
        @Negative(message = "El monto no puede ser menor a cero")
        BigDecimal amount,
        @NotNull(message = "La fecha de creación es requerida")
        @Past(message = "La fecha debe ser mayor a la fecha actual")
        LocalDateTime createdAt,
        UUID categoryId,
        UUID userId,
        String description,
        PaymentMethodEnum paymentMethod) implements Serializable {
}